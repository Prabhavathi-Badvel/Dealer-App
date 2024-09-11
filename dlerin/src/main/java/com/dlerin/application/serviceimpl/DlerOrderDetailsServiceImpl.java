package com.dlerin.application.serviceimpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerMaterialMaster;
import com.dlerin.application.entity.DlerOrderDetails;
import com.dlerin.application.entity.DlerOrderHeader;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerMaterialMasterRepo;
import com.dlerin.application.repository.DlerOrderDetailsRepo;
import com.dlerin.application.repository.DlerOrderHeaderRepo;
import com.dlerin.application.service.DlerOrderDetailsService;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
@Service
public class DlerOrderDetailsServiceImpl implements DlerOrderDetailsService {

	@Autowired
	DlerMaterialMasterRepo dlerMaterialMasterRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	DlerOrderDetailsRepo dlerOrderDetailsRepo;

	@Autowired
	DlerOrderHeaderRepo dlerOrderHeaderRepo;
	
	@Autowired
	private DlerBusinessLoginRepo dlerBusinessLoginRepo;
	
	@Autowired
	private JavaMailSender mailSender;

	private String generateOrderId() {
		LocalDateTime now = LocalDateTime.now();
		String year = String.valueOf(now.getYear());
		String month = String.format("%02d", now.getMonthValue());
		String day = String.format("%02d", now.getDayOfMonth());
		String hour = String.format("%02d", now.getHour());
		String minute = String.format("%02d", now.getMinute());
		String second = String.format("%02d", now.getSecond());
		String millis = String.format("%03d", now.getNano() / 1000000).substring(0, 2);
		return "Ord" + year + month + day + hour + minute + second + millis;
	}


	private String generateLineId(String generatedOrderId, int lineCounter) {
        return String.format("%s_%05d", generatedOrderId, lineCounter);
    }
	
	public List<DlerOrderDetails> addOrderDetailsToCart(List<DlerOrderDetails> orders) {
	    List<DlerOrderDetails> dods = new ArrayList<>();
	    DlerBusinessLogin dlerIdUser = null;
	    DlerBusinessLogin orderToUser = null;

	    DlerOrderHeader dlerHeader = null;

	    for (DlerOrderDetails order : orders) {
	        // Check if the material exists
	    	List<DlerMaterialMaster> materialList = dlerMaterialMasterRepo.findByDlerIdMaterialIdOne(order.getDlerIdMaterialId());
	    	if (materialList.isEmpty()) {
	    	    // If the list is empty, it means no matching material was found
	    	    return null;
	    	}
	    	DlerMaterialMaster material = materialList.get(0);
	        // Calculate the line item total price
	        int lineItemTotalPrice = calculateLineItemPrice(order);
	        order.setOrderTotal(lineItemTotalPrice);
	        order.setStatus("Pending");
	        order.setDlerId(material.getDlerId());

	        // Save the order details
	        DlerOrderDetails savedOrder = dlerOrderDetailsRepo.save(order);
	        savedOrder.setOrderTo(order.getOrderTo());
	        savedOrder.setDlerId(order.getDlerId());
	        savedOrder.setOrderTotal(lineItemTotalPrice);
	        savedOrder.setDeliveryTotal(calculateDeliveryAmount(order.getOrderId()));
	        dods.add(savedOrder);

	        // Check if the order header already exists for the given orderId
	        if (dlerHeader == null) {
	            dlerHeader = dlerOrderHeaderRepo.findByOrderId(order.getOrderId());
	        }

	        if (dlerHeader == null) {
	            // If the header doesn't exist, create a new one
	            dlerHeader = new DlerOrderHeader();
	            dlerHeader.setStatus(savedOrder.getStatus());
	            dlerHeader.setOrderId(savedOrder.getOrderId());
	            dlerHeader.setTotalAmount(lineItemTotalPrice); // Set initial total amount
	            dlerHeader.setOrderBy(savedOrder.getDlerId());
	            dlerHeader.setUpdatedBy(savedOrder.getDlerId());
	            dlerHeader.setOrderTo(savedOrder.getOrderTo());
	            dlerOrderHeaderRepo.save(dlerHeader);
	        } else {
	            // If the header exists, update the total amount
	            dlerHeader.setTotalAmount(dlerHeader.getTotalAmount() + lineItemTotalPrice);
	            dlerOrderHeaderRepo.save(dlerHeader);
	        }

	        // Get user information for email
	        dlerIdUser = dlerBusinessLoginRepo.findById(order.getDlerId()).orElse(null);
	        orderToUser = dlerBusinessLoginRepo.findById(order.getOrderTo()).orElse(null);
	    }

	    if (dlerHeader != null) {
	        updateInvoicedAmount(dlerHeader.getOrderId());
	    }

	    // Prepare and send email
	    String subject = "Order Details for Order ID: " + orders.get(0).getOrderId();
	    byte[] pdfContent = generateOrderPdfNew(dods);
	    String htmlContent = generateOrderHeaderHtmlNew(Collections.singletonList(dlerHeader));
	    try {
	        sendEmailWithPdfAndHtml(dlerIdUser.getDlerEmailId(), subject, pdfContent, htmlContent);
	        sendEmailWithPdfAndHtml(orderToUser.getDlerEmailId(), subject, pdfContent, htmlContent);
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }

	    return dods;
	}

	private int calculateLineItemPrice(DlerOrderDetails orderDetail) {
	    try {
	        int basePrice = orderDetail.getPricePerUnit();
	        int orderQty = orderDetail.getOrderQty();
	        int discount = orderDetail.getDiscount();
	        int gstPercentage = Integer.parseInt(orderDetail.getGst());
	        
	        int priceBeforeDiscount = basePrice * orderQty;
	        int priceAfterDiscount = priceBeforeDiscount - (priceBeforeDiscount * discount / 100);

	        int priceAfterGst = priceAfterDiscount + (priceAfterDiscount * gstPercentage / 100);
	        
	        return priceAfterGst;
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        return 0; // Return 0 or an appropriate default value in case of an error
	    }
	}

	public List<DlerOrderDetails> addOrders(List<DlerOrderDetails> orders) {
		List<DlerOrderDetails> addedOrders = new ArrayList<>();
		String generatedOrderId = generateOrderId(); // Generate Order ID only once
		int lineCounter = 1;

		// Generate line IDs and assign the order ID to each order detail
		for (DlerOrderDetails order : orders) {
			order.setOrderId(generatedOrderId);
			order.setLineId(generateLineId(generatedOrderId, lineCounter));
			lineCounter++;
		}

		// Add orders to the cart once all IDs are set
		List<DlerOrderDetails> addedOrder = addOrderDetailsToCart(orders);
		if (addedOrder != null) {
			addedOrders.addAll(addedOrder);
		}

		updateOrderHeaderTotalAmount(generatedOrderId);

		return addedOrders;
	}

	private int calculateTotalPrice(String orderId) {
		List<DlerOrderDetails> orderDetailsList = dlerOrderDetailsRepo.findByOrderId(orderId);

		int totalPrice = 0;
		for (DlerOrderDetails orderDetail : orderDetailsList) {
			try {
				int lineItemTotalPrice = calculateLineItemPrice(orderDetail);
		        totalPrice += lineItemTotalPrice;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return totalPrice;
	}

	private String generateOrderHeaderHtmlNew(List<DlerOrderHeader> headers) {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html><body>");
		htmlBuilder.append("<h2>Order Summary</h2>");

		// Start the table once
		htmlBuilder
				.append("<table border='1' cellpadding='5' cellspacing='0' style='width: 100%; margin-bottom: 20px;'>");

		// Add table headers

		DlerOrderHeader header1 = headers.get(0);
		String updatedByName1 = fetchNameByDlerId(header1.getUpdatedBy());
		htmlBuilder.append("<tr><th>Order ID</th><th>Status</th><th>Total Amount</th><th>Order By</th>").append("<th>")
				.append(updatedByName1).append("</th>").append("<th>Order To</th></tr>");

		// Iterate over the headers
		for (int i = 0; i < headers.size(); i++) {
			DlerOrderHeader header = headers.get(i);

			// Fetching names using the provided method
			String orderByName = fetchNameByDlerId(header.getOrderBy());
			String updatedByName = fetchNameByDlerId(header.getUpdatedBy());
			String orderByToName = fetchNameByDlerId(header.getOrderTo());

			// Add a row for each header
			htmlBuilder.append("<tr>").append("<td>").append(header.getOrderId()).append("</td>").append("<td>")
					.append(header.getStatus()).append("</td>").append("<td>").append(header.getTotalAmount())
					.append("</td>").append("<td>").append(header.getOrderBy()).append(" (").append(orderByName)
					.append(")</td>").append("<td>").append(header.getUpdatedBy()).append(" (").append(updatedByName)
					.append(")</td>").append("<td>").append(header.getOrderTo()).append(" (").append(orderByToName)
					.append(")</td>").append("</tr>");
		}

		// End the table
		htmlBuilder.append("</table>");

		htmlBuilder.append("<h2>Please log in to your account to see order details</h2>");
		htmlBuilder.append("</body></html>");
		return htmlBuilder.toString();
	}

	private byte[] generateOrderPdfNew(List<DlerOrderDetails> details) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		try (PdfWriter writer = new PdfWriter(byteArrayOutputStream)) {
			PdfDocument pdfDoc = new PdfDocument(writer);
			Document document = new Document(pdfDoc, PageSize.A4);

			// Set margins to ensure the content fits within the A4 page
			document.setMargins(20, 20, 20, 20);

			// Add a title with a larger font size
			document.add(new Paragraph("Order Details").setFontSize(14).setBold());

			// Create a table with 10 columns, adjusting widths to better fit content
			Table table = new Table(new float[] { 2.5f, // Line ID (slightly wider for long text)
					2.0f, // Material ID
					1.5f, // Order Quantity
					1.5f, // Delivery Quantity
					2.0f, // Status
					1.8f, // Price Per Unit
					1.5f, // GST
					2.0f, // GST Code
					2.5f, // Order ID (slightly wider for long text)
					1.5f // Discount
			});

			table.setWidth(UnitValue.createPercentValue(100)); // Set table width to 100% of the available space

			// Define a slightly smaller font size
			float smallFontSize = 5.5f;

			// Add header cells
			table.addHeaderCell(new Paragraph("Line ID").setFontSize(smallFontSize).setBold());
			table.addHeaderCell(new Paragraph("Material ID").setFontSize(smallFontSize).setBold());
			table.addHeaderCell(new Paragraph("Order Quantity").setFontSize(smallFontSize).setBold());
			table.addHeaderCell(new Paragraph("Delivery Quantity").setFontSize(smallFontSize).setBold());
			table.addHeaderCell(new Paragraph("Status").setFontSize(smallFontSize).setBold());
			table.addHeaderCell(new Paragraph("Price Per Unit").setFontSize(smallFontSize).setBold());
			table.addHeaderCell(new Paragraph("GST").setFontSize(smallFontSize).setBold());
			table.addHeaderCell(new Paragraph("GST Code").setFontSize(smallFontSize).setBold());
			table.addHeaderCell(new Paragraph("Order ID").setFontSize(smallFontSize).setBold());
			table.addHeaderCell(new Paragraph("Discount").setFontSize(smallFontSize).setBold());

			// Add data cells
			for (DlerOrderDetails detail : details) {
				table.addCell(new Paragraph(detail.getLineId()).setFontSize(smallFontSize));
				table.addCell(new Paragraph(detail.getDlerIdMaterialId()).setFontSize(smallFontSize));
				table.addCell(new Paragraph(String.valueOf(detail.getOrderQty())).setFontSize(smallFontSize));
				table.addCell(new Paragraph(String.valueOf(detail.getDeliveredQty())).setFontSize(smallFontSize));
				table.addCell(new Paragraph(detail.getStatus()).setFontSize(smallFontSize));
				table.addCell(new Paragraph(String.valueOf(detail.getPricePerUnit())).setFontSize(smallFontSize));
				table.addCell(new Paragraph(String.valueOf(detail.getGst())).setFontSize(smallFontSize));
				table.addCell(new Paragraph(detail.getGstCode()).setFontSize(smallFontSize));
				table.addCell(new Paragraph(detail.getOrderId()).setFontSize(smallFontSize));
				table.addCell(new Paragraph(String.valueOf(detail.getDiscount())).setFontSize(smallFontSize));
			}

			// Add the table to the document
			document.add(table);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return byteArrayOutputStream.toByteArray();
	}

	private String fetchNameByDlerId(String dlerId) {
		return dlerBusinessLoginRepo.findById(dlerId).map(DlerBusinessLogin::getDlerName).orElse("Unknown");
	}

	private void sendEmailWithPdfAndHtml(String to, String subject, byte[] pdfContent, String htmlContent)
			throws MessagingException {
		if (to != null) {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates multipart

			try {
				helper.setTo(to);
				helper.setSubject(subject);
				helper.setText(htmlContent, true); // true indicates HTML content

				// Attach the PDF file
				InputStreamSource attachmentSource = new ByteArrayResource(pdfContent);
				helper.addAttachment("OrderDetails.pdf", attachmentSource);

				mailSender.send(message);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateOrderHeaderTotalAmount(String orderId) {
		DlerOrderHeader dlerHeader = dlerOrderHeaderRepo.findByOrderId(orderId);

		if (dlerHeader != null) {
			int totalAmount = calculateTotalPrice(orderId);
			dlerHeader.setTotalAmount(totalAmount);
			dlerOrderHeaderRepo.save(dlerHeader);
		}
	}

	@Override
	public List<DlerOrderDetails> updateOrder(List<DlerOrderDetails> orders) {
	    List<DlerOrderDetails> updatedOrders = new ArrayList<>();
	    StringBuilder messageBuilder = new StringBuilder(); // To collect messages for the response

	    for (DlerOrderDetails order : orders) {
	        Optional<DlerOrderDetails> lineIdOptional = dlerOrderDetailsRepo.findById(order.getLineId());

	        if (lineIdOptional.isPresent()) {
	            DlerOrderDetails dbOrderDetails = lineIdOptional.get();

	            // Check if the order quantity is more than the delivery quantity
	            if (dbOrderDetails.getOrderQty() >= order.getDeliveredQty()) {
	                // Set the status and delivered quantity from the input order
	                dbOrderDetails.setStatus(order.getStatus());
	                dbOrderDetails.setDeliveredQty(order.getDeliveredQty());

	                // Dynamically generate the remark based on deliveredQty
	                String remark = "DELIVERED " + order.getDeliveredQty() + " products";
	                dbOrderDetails.setRemark(remark);

	                // Calculate and update the deliveryTotal for this specific line item
	                int deliveryTotal = calculateLineItemDeliveryAmount(dbOrderDetails);
	                dbOrderDetails.setDeliveryTotal(deliveryTotal);

	                // Save the updated order details
	                DlerOrderDetails updatedOrderDetails = dlerOrderDetailsRepo.save(dbOrderDetails);
	                updatedOrders.add(updatedOrderDetails);
	            } else {
	                // When deliveredQty exceeds orderQty, fetch material name and generate error message
	                String dlerIdMaterialId = dbOrderDetails.getDlerIdMaterialId();

	                // Fetch the material from DlerMaterialMaster based on dlerIdMaterialId
	                List<DlerMaterialMaster> materialList = dlerMaterialMasterRepo.findByDlerIdMaterialIdOne(dlerIdMaterialId);
	                if (!materialList.isEmpty()) {
	                    DlerMaterialMaster material = materialList.get(0);
	                    String materialName = material.getMaterialName();

	                    // Append message for the error case
	                    messageBuilder.append("Error: Delivered quantity exceeds order quantity for material ")
	                            .append(materialName)
	                            .append(". Delivered: ")
	                            .append(order.getDeliveredQty())
	                            .append(", Ordered: ")
	                            .append(dbOrderDetails.getOrderQty())
	                            .append(". ");
	                }
	            }
	        }
	    }

	    // Update the invoiced amount if there are any updated orders
	    if (!updatedOrders.isEmpty()) {
	        updateInvoicedAmount(updatedOrders.get(0).getOrderId());
	    }

	    // If there are error messages, add them to the final response
	    if (messageBuilder.length() > 0) {
	        throw new RuntimeException(messageBuilder.toString()); // This will propagate the error message to the controller
	    }

	    return updatedOrders;
	}



	public void sendEmailWithHtml(String to, String subject, String htmlContent) throws MessagingException {
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	    helper.setTo(to);
	    helper.setSubject(subject);
	    helper.setText(htmlContent, true);

	    mailSender.send(message);
	}


	private int calculateLineItemDeliveryAmount(DlerOrderDetails orderDetail) {
	    try {
	        int basePrice = orderDetail.getPricePerUnit();
	        int deliveredQty = orderDetail.getDeliveredQty();
	        int discount = orderDetail.getDiscount();
	        int gstPercentage = Integer.parseInt(orderDetail.getGst());
	        
	        int priceBeforeDiscount = basePrice * deliveredQty;
	        int priceAfterDiscount = priceBeforeDiscount - (priceBeforeDiscount * discount / 100);
	        int priceAfterGst = priceAfterDiscount + (priceAfterDiscount * gstPercentage / 100);
	        
	        return priceAfterGst;
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        return 0; // Return 0 or an appropriate default value in case of an error
	    }
	}



	private void updateInvoicedAmount(String orderId) {
		Integer invoicedAmount = calculateDeliveryAmount(orderId);
		DlerOrderHeader orderHeader = dlerOrderHeaderRepo.findByOrderId(orderId);
		if (orderHeader != null) {
			orderHeader.setToBeInvoicedAmount(invoicedAmount != null ? invoicedAmount : 0);

			dlerOrderHeaderRepo.save(orderHeader);
		}else {
	        // Debugging step: Log if no header found
	        System.out.println("No OrderHeader found for Order ID " + orderId);
	    }
	}

	private int calculateDeliveryAmount(String orderId) {
	    List<DlerOrderDetails> orderDetailsList = dlerOrderDetailsRepo.findByOrderId(orderId);
	    int totalPrice = 0;
	    for (DlerOrderDetails orderDetail : orderDetailsList) {
	        try {
	            int basePrice = orderDetail.getPricePerUnit();
	            int deliveredQty = orderDetail.getDeliveredQty();
	            int discount = orderDetail.getDiscount();
	            int gstPercentage = Integer.parseInt(orderDetail.getGst());
	            
	            int priceBeforeDiscount = basePrice * deliveredQty;
	            int priceAfterDiscount = priceBeforeDiscount - (priceBeforeDiscount * discount / 100);
	            int priceAfterGst = priceAfterDiscount + (priceAfterDiscount * gstPercentage / 100);

	            totalPrice += priceAfterGst;
	        } catch (NumberFormatException e) {
	            e.printStackTrace();
	        }
	    }
	    return totalPrice;
	}
}
