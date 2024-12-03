package com.dlerin.application.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dlerin.application.entity.MembershipRenewalDetails;
import com.dlerin.application.entity.MembershipRenewalHeader;
import com.dlerin.application.service.MembershipRenewalDetailsService;

@Service
public class MembershipRenewalDetailsServiceImpl implements MembershipRenewalDetailsService {

	@Override
	public MembershipRenewalHeader processMembershipRenewal(List<MembershipRenewalDetails> detailsList) {
		// TODO Auto-generated method stub
		return null;
	}


//	@Autowired
//	private StoreMembershipRepo membershipRepo;
//	
//	@Autowired
//	private MembershipRenewalDetailsRepo membershipRenewalDetailsRepo;
//	
//	@Autowired
//	private MembershipRenewalHeaderRepo membershipRenewalHeaderRepo;
//	
//	@Autowired
//	private DlerBusinessLoginRepo dlerBusinessLoginRepo;
//	private String generateOrderId() {
//		LocalDateTime now = LocalDateTime.now();
//		String year = String.valueOf(now.getYear());
//		String month = String.format("%02d", now.getMonthValue());
//		String day = String.format("%02d", now.getDayOfMonth());
//		String hour = String.format("%02d", now.getHour());
//		String minute = String.format("%02d", now.getMinute());
//		String second = String.format("%02d", now.getSecond());
//		String millis = String.format("%03d", now.getNano() / 1000000).substring(0, 2);
//		return "MEM" + year + month + day + hour + minute + second + millis;
//	}
//	
//	private String generateLineId(String generatedOrderId, int lineCounter) {
//        return String.format("%s_%05d", generatedOrderId, lineCounter);
//    }
//	private int calculateLineItemPrice(MembershipRenewalDetails membershipRenewalDetails) {
//	    try {
//	        int membershipRenewalDetailsAmount = membershipRenewalDetails.getOrderAmount();     
//	        return membershipRenewalDetailsAmount;
//	    } catch (NumberFormatException e) {
//	        e.printStackTrace();
//	        return 0;
//	    }
//	}
//	private List<MembershipRenewalDetails> addMembershipRenewalDetailsToCart(List<MembershipRenewalDetails> orders) {
//	    List<MembershipRenewalDetails> dods = new ArrayList<>();
//	    DlerBusinessLogin dlerIdUser = null;
//	    DlerBusinessLogin orderToUser = null;
//
//	    MembershipRenewalHeader membershipHeader = null;
//
//	    for (MembershipRenewalDetails order : orders) {
//	        // Check if the material exists
//	    	List<StoreMembership> storeList = membershipRepo.findByStoreIdKey(order.getStoreId());
//	    	if (storeList.isEmpty()) {
//	    	    // If the list is empty, it means no matching material was found
//	    	    return null;
//	    	}
//	    	StoreMembership store = storeList.get(0);
//	        // Calculate the line item total price
//	        int lineItemTotalPrice = calculateLineItemPrice(order);
//	        order.setOrderAmount(lineItemTotalPrice);
//	        order.setStatus("NEW");
//	        order.setOrderPlacedBy(store.getDlerId());
//
//	        // Save the order details
//	        MembershipRenewalDetails savedOrder = membershipRenewalDetailsRepo.save(order);
////	        savedOrder.setOrderTo(order.getOrderTo());
////	        savedOrder.setDlerId(order.getDlerId());
//	        savedOrder.setOrderAmount(lineItemTotalPrice);
//	        savedOrder.setOrderAmount(calculateDeliveryAmount(order.getMembershipOrderId()));
//	        dods.add(savedOrder);
//
//	        // Check if the order header already exists for the given orderId
//	        if (membershipHeader == null) {
//	        	membershipHeader = membershipRenewalHeaderRepo.findByMembershipOrderId(order.getMembershipOrderId());
//	        }
//
//	        if (membershipHeader == null) {
//	            // If the header doesn't exist, create a new one
//	        	membershipHeader = new MembershipRenewalHeader();
//	        	membershipHeader.setStatus(savedOrder.getStatus());
//	        	membershipHeader.setMembershipOrderId(savedOrder.getMembershipOrderId());
//	        	membershipHeader.setOrderAmount(lineItemTotalPrice); 
//	        	membershipHeader.setOrderPlacedBy(savedOrder.getOrderPlacedBy());
//	        	membershipRenewalHeaderRepo.save(membershipHeader);
//	        } else {
//	            // If the header exists, update the total amount
//	        	membershipHeader.setOrderAmount(membershipHeader.getOrderAmount() + lineItemTotalPrice);
//	        	membershipRenewalHeaderRepo.save(membershipHeader);
//	        }
//
//	        // Get user information for email
//	        dlerIdUser = dlerBusinessLoginRepo.findById(order.getOrderPlacedBy()).orElse(null);
//	    }
//
//	    if (membershipHeader != null) {
//	        updateInvoicedAmount(membershipHeader.getMembershipOrderId());
//	    }
//
//	    // Prepare and send email
//	    String subject = "Order Details for Order ID: " + orders.get(0).getOrderId();
//	    byte[] pdfContent = generateOrderPdfNew(dods);
//	    String htmlContent = generateOrderHeaderHtmlNew(Collections.singletonList(dlerHeader));
//	    try {
//	        sendEmailWithPdfAndHtml(dlerIdUser.getDlerEmailId(), subject, pdfContent, htmlContent);
//	        sendEmailWithPdfAndHtml(orderToUser.getDlerEmailId(), subject, pdfContent, htmlContent);
//	    } catch (MessagingException e) {
//	        e.printStackTrace();
//	    }
//
//	    return dods;
//	}
//	@Override
//	public List<MembershipRenewalDetails> addOrders(List<MembershipRenewalDetails> orders) throws MessagingException {
//		
//		List<MembershipRenewalDetails> addedOrders = new ArrayList<>();
//		String generatedOrderId = generateOrderId(); // Generate Order ID only once
//		int lineCounter = 1;
//
//		// Generate line IDs and assign the order ID to each order detail
//		for (MembershipRenewalDetails order : orders) {
//			order.setMembershipOrderId(generatedOrderId);
//			order.setMembershipOrderIdLineItem(generateLineId(generatedOrderId, lineCounter));
//			lineCounter++;
//		}
//
//		// Add orders to the cart once all IDs are set
//		List<MembershipRenewalDetails> addedOrder = addMembershipRenewalDetailsToCart(orders);
//		if (addedOrder != null) {
//			addedOrders.addAll(addedOrder);
//		}
//
//		updateOrderHeaderTotalAmount(generatedOrderId);
//
//		return addedOrders;
//	}
//	private void updateInvoicedAmount(String membershipOrderId) {
//		Integer invoicedAmount = calculateDeliveryAmount(membershipOrderId);
//		MembershipRenewalHeader orderHeader = membershipRenewalHeaderRepo.findByMembershipOrderId(membershipOrderId);
//		if (orderHeader != null) {
//			orderHeader.setToBeInvoicedAmount(invoicedAmount != null ? invoicedAmount : 0);
//
//			dlerOrderHeaderRepo.save(orderHeader);
//		}else {
//	        // Debugging step: Log if no header found
//	        System.out.println("No OrderHeader found for Order ID " + orderId);
//	    }
//	}
//	private int calculateDeliveryAmount(String orderId) {
//	    List<MembershipRenewalDetails> orderDetailsList = membershipRenewalDetailsRepo.findByMembershipOrderId(orderId);
//	    int totalPrice = 0;
//	    for (MembershipRenewalDetails orderDetail : orderDetailsList) {
//	        try {
//	            int amount = orderDetail.getOrderAmount();
//	            totalPrice += amount;
//	        } catch (NumberFormatException e) {
//	            e.printStackTrace();
//	        }
//	    }
//	    return totalPrice;
//	}
}
