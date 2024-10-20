package com.dlerin.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.ResponseDlerOrderDetails;
import com.dlerin.application.dto.ResponseDlerOrderDetails1;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerOrderDetails;
import com.dlerin.application.entity.DlerOrderHeader;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerOrderHeaderRepo;
import com.dlerin.application.service.DlerOrderDetailsService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@RestController
@PreAuthorize("hasAuthority('Dealer')")
public class DlerOrderDetailsController {

	@Autowired
	private DlerOrderDetailsService orderService;
	
	@Autowired
	private DlerOrderHeaderRepo dlerOrderHeaderRepo;
	
	@Autowired
	private DlerBusinessLoginRepo dlerBusinessLoginRepo;
	
	@Autowired
	private JavaMailSender mailSender;

	@PostMapping("/dlerin-add-orders")
	public ResponseEntity<?> addOrderDetails(@RequestBody List<DlerOrderDetails> order) throws MessagingException {
		ResponseDlerOrderDetails response = new ResponseDlerOrderDetails();
		List<DlerOrderDetails> orderDetailsList = orderService.addOrders(order);
		try {
			
			if (!orderDetailsList.isEmpty()) {
				response.setMessage(orderDetailsList.get(0).getOrderId()+"(OrderId) is placed successfully");
				response.setStatus(true);
				response.setOrderData(orderDetailsList);
				return new ResponseEntity<>(response, HttpStatus.OK);

			} else {
				response.setMessage("failed to add/dlerId or materialId is not present");
				response.setStatus(false);
				response.setOrderData(orderDetailsList);
				return new ResponseEntity<>(response, HttpStatus.OK);

			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus(false);
			response.setOrderData(orderDetailsList);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PutMapping("/dlerin-update-dlerorderdetails")
	public ResponseEntity<?> updateOrderDetails(@RequestBody List<DlerOrderDetails> orders) {
	    ResponseDlerOrderDetails1 response1 = new ResponseDlerOrderDetails1();
	    try {
	        List<DlerOrderDetails> updateOrders = orderService.updateOrder(orders);
	        if (!updateOrders.isEmpty()) {
	            StringBuilder messageBuilder = new StringBuilder();

	            for (int i = 0; i < updateOrders.size(); i++) {
	                messageBuilder.append("Order number ")
	                              .append(updateOrders.get(i).getLineId())
	                              .append(" is updated to ")
	                              .append(updateOrders.get(i).getStatus());

	                if (i < updateOrders.size() - 1) {
	                    messageBuilder.append(", "); // Add a comma except after the last entry
	                }
	            }

	            // Setting response message
	            response1.setMessage(messageBuilder.toString());
	            response1.setStatus(true);

	            // Fetching orderId to find DlerOrderHeader
	            String orderId = updateOrders.get(0).getOrderId();
	            Optional<DlerOrderHeader> orderHeaderOptional = dlerOrderHeaderRepo.findById(orderId);

	            if (orderHeaderOptional.isPresent()) {
	                DlerOrderHeader orderHeader = orderHeaderOptional.get();

	                // Fetch orderBy and orderTo
	                String orderBy = orderHeader.getOrderBy();
	                String orderTo = orderHeader.getOrderTo();

	                // Fetch email addresses from DlerBusinessLogin based on orderBy and orderTo
	                Optional<DlerBusinessLogin> orderByUser = dlerBusinessLoginRepo.findById(orderBy);
	                Optional<DlerBusinessLogin> orderToUser = dlerBusinessLoginRepo.findById(orderTo);

	                if (orderByUser.isPresent() && orderToUser.isPresent()) {
	                    String orderByEmail = orderByUser.get().getDlerEmailId();
	                    String orderToEmail = orderToUser.get().getDlerEmailId();

	                    // Send email to both orderBy and orderTo
	                    sendEmail(orderByEmail, "Dler Order Header Update", response1.getMessage());
	                    sendEmail(orderToEmail, "Dler Order Header Update", response1.getMessage());
	                }
	            }

	            return new ResponseEntity<>(response1, HttpStatus.OK);
	        } else {
	            response1.setMessage("Failed to update/order ID not found");
	            response1.setStatus(false);
	            return new ResponseEntity<>(response1, HttpStatus.OK);
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}

	// Method to send an email using JavaMailSender
	private void sendEmail(String toEmail, String subject, String message) {
	    try {
	        MimeMessage mail = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
	        
	        helper.setFrom("no_reply@kosuriers.com");
	        helper.setTo(toEmail);
	        helper.setSubject(subject);
	        helper.setText(message, true); // true means the content is HTML
	        mailSender.send(mail);
	        System.out.println("Email sent to " + toEmail);
	    } catch (MessagingException e) {
	        e.printStackTrace();
	        System.out.println("Error sending email to " + toEmail);
	    }
	}



	private String generateOrderUpdateHtml(String message) {
	    return "<html>" +
	           "<body>" +
	           "<h2>Order Update Notification</h2>" +
	           "<p>" + message + "</p>" +
	           "</body>" +
	           "</html>";
	}
	public void sendEmailWithHtml(String to, String subject, String htmlContent) throws MessagingException {
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	    helper.setFrom("no_reply@kosuriers.com");
	    helper.setTo(to);
	    helper.setSubject(subject);
	    helper.setText(htmlContent, true);

	    mailSender.send(message);
	}


}
