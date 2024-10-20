package com.dlerin.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.ResponseDlerOrderHeaderDto;
import com.dlerin.application.dto.ResponseHeaderDto;
import com.dlerin.application.dto.ResponseListOrderDto;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerOrderDetails;
import com.dlerin.application.entity.DlerOrderHeader;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.service.DlerOrderHeaderService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@RestController
@PreAuthorize("hasAuthority('Dealer')")
public class DlerOrderHeaderController {

	@Autowired
	DlerOrderHeaderService dlerOrderHeaderService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private DlerBusinessLoginRepo dlerBusinessLoginRepo;

	@PutMapping("/update-dlerorderheader")
	public ResponseEntity<?> updateHeader(@RequestBody DlerOrderHeader header) {
		ResponseDlerOrderHeaderDto response = new ResponseDlerOrderHeaderDto();
		try {
			// Update the order header details
			DlerOrderHeader update = dlerOrderHeaderService.updateHeaderDetails(header);
			if (update != null) {
				// Construct the response message
				String message = "Order number " + update.getOrderId() + " is updated to " + update.getStatus();
				response.setMessage(message);
				response.setStatus(true);

				// Fetch email addresses for orderBy and orderTo
				String orderBy = update.getOrderBy();
				String orderTo = update.getOrderTo();

				// Retrieve email addresses from DlerBusinessLogin
				DlerBusinessLogin orderByUser = dlerBusinessLoginRepo.findByDlerUserId(orderBy);
				DlerBusinessLogin orderToUser = dlerBusinessLoginRepo.findByDlerUserId(orderTo);

				if (orderByUser != null && orderToUser != null) {
					String orderByEmail = orderByUser.getDlerEmailId();
					String orderToEmail = orderToUser.getDlerEmailId();

					// Send email to orderBy and orderTo
					sendEmail(orderByEmail, "Order Update", message);
					sendEmail(orderToEmail, "Order Update", message);
				} else {
					System.out.println("User(s) not found for orderBy or orderTo.");
				}

				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Failed to update / order ID not present");
				response.setStatus(false);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	private void sendEmail(String toEmail, String subject, String message) {
		try {
			MimeMessage mail = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mail, true);
			
			helper.setFrom("no_reply@kosuriers.com");
			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(message, true); // true for HTML content
			mailSender.send(mail);
			System.out.println("Email sent to " + toEmail);
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println("Error sending email to " + toEmail);
		}
	}

	@GetMapping("/orderdetails")
	public ResponseEntity<?> getOrders(

			@RequestParam(required = false) String orderId, @RequestParam(required = false) String orderBy,
			@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate,
			@RequestParam(required = false) String orderTo) {
		ResponseListOrderDto response1 = new ResponseListOrderDto();
		try {
			List<DlerOrderDetails> entity = dlerOrderHeaderService.getOrderData(orderId, orderBy, fromDate, toDate,
					orderTo);
			if (!entity.isEmpty()) {
				response1.setMessage("Order details fetched successfully.!");
				response1.setStatus(true);
				response1.setData(entity);
				return ResponseEntity.ok(response1);
			} else {
				response1.setMessage("No data found for the given details/check your parameters!");
				response1.setStatus(true);
				return ResponseEntity.status(HttpStatus.OK).body(response1);
			}
		} catch (Exception e) {
			response1.setMessage(e.getMessage());
			response1.setStatus(false);
			return ResponseEntity.status(HttpStatus.OK).body(response1);
		}

	}

	@GetMapping("/getorderheaderdetails")
	public ResponseEntity<?> getOrderHeader(

			@RequestParam(required = false) String orderId, @RequestParam(required = false) String orderBy,
			@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate,
			@RequestParam(required = false) String orderTo) {
		ResponseHeaderDto response1 = new ResponseHeaderDto();
		try {
			List<DlerOrderHeader> header = dlerOrderHeaderService.getOrderHeaderData(orderId, orderBy, fromDate, toDate,
					orderTo);
			if (!header.isEmpty()) {
				response1.setMessage("header details fetched successfully.!");
				response1.setStatus(true);
				response1.setData(header);
				return ResponseEntity.ok(response1);
			} else {
				response1.setMessage("No data found for the given details/check your parameters!");
				response1.setStatus(false);
				return ResponseEntity.status(HttpStatus.OK).body(response1);
			}
		} catch (Exception e) {
			response1.setMessage(e.getMessage());
			response1.setStatus(false);
			return ResponseEntity.status(HttpStatus.OK).body(response1);
		}

	}

}
