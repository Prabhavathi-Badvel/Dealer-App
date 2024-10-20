package com.dlerin.application.serviceimpl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerInvoiceDetails;
import com.dlerin.application.entity.DlerOrderHeader;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerInvoiceDetailsRepo;
import com.dlerin.application.repository.DlerOrderHeaderRepo;
import com.dlerin.application.service.DlerInvoiceDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.internet.MimeMessage;

@Service
public class DlerInvoiceDetailsServiceImpl implements DlerInvoiceDetailsService {

	@Autowired
	private DlerOrderHeaderRepo dlerOrderHeaderRepo;

	@Autowired
	private DlerInvoiceDetailsRepo invoiceRepo;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private DlerBusinessLoginRepo dlerBusinessLoginRepo;

	@Override
	public DlerInvoiceDetails saveInvoceDetails(DlerInvoiceDetails details) {
	    Optional<DlerOrderHeader> orderIdExists = Optional.ofNullable(dlerOrderHeaderRepo.findByOrderId(details.getOrderId()));

	    if (orderIdExists.isPresent()) {
	        DlerOrderHeader db = orderIdExists.get();

	        // Check if the invoice is already generated
	        if (db.getInvoiceNo() != null) {
	            // Return a message or handle the case where the invoice is already generated
	            throw new IllegalStateException("Invoice already generated with ID: " + db.getInvoiceNo());
	            // Alternatively, you could return a null or custom response instead of throwing an exception.
	        }

	        // Set the necessary fields in DlerInvoiceDetails
	        details.setInviceTo(db.getOrderTo());
	        details.setUpdateBy(db.getUpdatedBy());
	        details.setOrderId(db.getOrderId());
	        details.setTotalAmount(db.getTotalAmount());

	        // Save the DlerInvoiceDetails
	        DlerInvoiceDetails savedInvoiceDetails = invoiceRepo.save(details);

	        // Update the invoiceNo in DlerOrderHeader with the generatedInvoiceId
	        db.setInvoiceNo(savedInvoiceDetails.getGeneratedInvoiceId());

	        // Save the updated DlerOrderHeader
	        dlerOrderHeaderRepo.save(db);

	        // Fetch email addresses and names using invoice_to and updated_by
	        DlerBusinessLogin invoiceToUser = dlerBusinessLoginRepo.findById(db.getOrderTo()).orElse(null);
	        DlerBusinessLogin updatedByUser = dlerBusinessLoginRepo.findById(db.getUpdatedBy()).orElse(null);

	        // Convert DlerInvoiceDetails to HTML
	        String jsonContent = convertToHtml(savedInvoiceDetails);

	        // Send email to both invoice_to and updated_by with dlerId and dlerName
	        if (invoiceToUser != null) {
	            sendEmail(invoiceToUser.getDlerEmailId(), 
	                      "Invoice Generated: " + savedInvoiceDetails.getGeneratedInvoiceId(), 
	                      jsonContent, 
	                      invoiceToUser.getDlerUserId(), 
	                      invoiceToUser.getDlerName());
	        }

	        if (updatedByUser != null) {
	            sendEmail(updatedByUser.getDlerEmailId(), 
	                      "Invoice Generated: " + savedInvoiceDetails.getGeneratedInvoiceId(), 
	                      jsonContent, 
	                      updatedByUser.getDlerUserId(), 
	                      updatedByUser.getDlerName());
	        }

	        return savedInvoiceDetails;
	    }

	    return null;
	}


	    private String fetchEmailByDlerId(String dlerId) {
	        return dlerBusinessLoginRepo.findById(dlerId)
	                .map(DlerBusinessLogin::getDlerEmailId)
	                .orElse(null);
	    }

	    private void sendEmail(String to, String subject, String body, String dlerId, String dlerName) {
	        if (to != null) {
	            try {
	                MimeMessage message = mailSender.createMimeMessage();
	                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	                helper.setFrom("no_reply@kosuriers.com");
	                helper.setTo(to);
	                helper.setSubject(subject);

	                // Modify the body to include dlerId and dlerName
	                String emailBody = String.format("Dear %s (%s),<br><br>%s<br><br>Best regards,<br>Your Company", dlerName, dlerId, body);

	                helper.setText(emailBody, true); // Set to true to enable HTML content

	                mailSender.send(message);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }


	    private String convertToHtml(DlerInvoiceDetails details) {
	        StringBuilder htmlBuilder = new StringBuilder();

	        // Fetch the names associated with Invoice To and Updated By
	        String invoiceToName = fetchNameByDlerId(details.getInviceTo());
	        String updatedByName = fetchNameByDlerId(details.getUpdateBy());

	        htmlBuilder.append("<html>");
	        htmlBuilder.append("<head>")
	                   .append("<title>Invoice Details</title>")
	                   .append("<style>")
	                   .append("body { background-color: #f0f0f0; font-family: Arial, sans-serif; }")
	                   .append("h1 { color: #333333; }")
	                   .append("table { background-color: #ffffff; width: 100%; border-collapse: collapse; }")
	                   .append("th, td { padding: 10px; border: 1px solid #dddddd; text-align: left; }")
	                   .append("th { background-color: #4CAF50; color: white; }")
	                   .append("</style>")
	                   .append("</head>");
	        htmlBuilder.append("<body>");
	        htmlBuilder.append("<h1>Invoice Details</h1>");
	        htmlBuilder.append("<table>");

//	        htmlBuilder.append("<tr><th>Field</th><th>Value</th></tr>");
	        htmlBuilder.append("<tr><td>Generated Invoice ID</td><td>").append(details.getGeneratedInvoiceId()).append("</td></tr>");
	        htmlBuilder.append("<tr><td>Order ID</td><td>").append(details.getOrderId()).append("</td></tr>");
	        
	        // Include Invoice To with dlerName in brackets
	        htmlBuilder.append("<tr><td>Invoice To</td><td>")
	                   .append(details.getInviceTo())
	                   .append(" (").append(invoiceToName).append(")")
	                   .append("</td></tr>");
	        
	        DlerBusinessLogin businessLogin=new DlerBusinessLogin();
	     // Set the supplierName variable
	        String supplierName = String.valueOf(updatedByName); // Assuming "Updated By" is the label you want

	        // Include Updated By with dlerName in brackets
	        htmlBuilder.append("<tr><td>")
	                   .append(supplierName)  // Use supplierName here
	                   .append("</td><td>")
	                   .append(details.getUpdateBy())
	                   .append(" (").append(updatedByName).append(")")
	                   .append("</td></tr>");

	        htmlBuilder.append("<tr><td>Total Amount</td><td>").append(details.getTotalAmount()).append("</td></tr>");

	        // Add other fields as necessary

	        htmlBuilder.append("</table>");
	        htmlBuilder.append("</body>");
	        htmlBuilder.append("</html>");

	        return htmlBuilder.toString();
	    }

	    private String fetchNameByDlerId(String dlerId) {
	        return dlerBusinessLoginRepo.findById(dlerId)
	                .map(DlerBusinessLogin::getDlerName)
	                .orElse("Unknown");
	    }


}
