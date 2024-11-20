package com.dlerin.application.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerOrderDetails;
import com.dlerin.application.entity.DlerOrderHeader;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerOrderDetailsRepo;
import com.dlerin.application.repository.DlerOrderHeaderRepo;
import com.dlerin.application.service.DlerOrderHeaderService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class DlerOrderHeaderServiceImpl implements DlerOrderHeaderService {

	@Autowired
	DlerOrderDetailsRepo dlerOrderDetailsRepo;

	@Autowired
	DlerOrderHeaderRepo dlerOrderHeaderRepo;

	@Autowired
	private DlerBusinessLoginRepo dlerBusinessLoginRepo;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public DlerOrderHeader updateHeaderDetails(DlerOrderHeader header) {
	    Optional<DlerOrderHeader> orderId = dlerOrderHeaderRepo.findById(header.getOrderId());

	    if (orderId.isPresent()) {
	        DlerOrderHeader db = orderId.get();
	        db.setRemarks(header.getRemarks());
	        db.setStatus(header.getStatus());
	        return dlerOrderHeaderRepo.save(db);
	    }
	    return null;
	}

//	public void sendUpdateEmail(DlerOrderHeader update, String message) {
//	    try {
//	        // Get the email address of the user related to the order
//	        DlerBusinessLogin dlerUser = dlerBusinessLoginRepo.findById(update.getUpdatedBy()).orElse(null);
//	        if (dlerUser != null) {
//	            String subject = "Order Update Notification for Order ID: " + update.getOrderId();
//	            String htmlContent = generateHtmlContent(message);
//	            sendEmail(dlerUser.getDlerEmailId(), subject, htmlContent);
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	}

//	private String generateHtmlContent(String message) {
//	    // Here, you can format the message as HTML
//	    return "<html><body><p>" + message + "</p></body></html>";
//	}

//	private void sendEmail(String to, String subject, String htmlContent) {
//	    // Implement your email sending logic here
//	    try {
//	        // For example, using JavaMailSender
//	        MimeMessage message = mailSender.createMimeMessage();
//	        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
//	        helper.setText(htmlContent, true);
//	        helper.setTo(to);
//	        helper.setSubject(subject);
//	        helper.setFrom("your-email@example.com");
//	        mailSender.send(message);
//	    } catch (MessagingException e) {
//	        e.printStackTrace();
//	    }
//	}

	@Override
	public List<DlerOrderDetails> getOrderData(String orderId, String orderBy, String fromDate, String toDate,
			String orderTo) {
		List<DlerOrderHeader> headers = new ArrayList<>();
		List<DlerOrderDetails> orderDetails = new ArrayList<>();

		if (orderId != null) {
			DlerOrderHeader header = dlerOrderHeaderRepo.findByOrderId(orderId);
			if (header != null) {
				orderDetails = dlerOrderDetailsRepo.findByOrderIdIn(Collections.singletonList(header.getOrderId()));
	            setDlerIdAndOrderTo(orderDetails, Collections.singletonList(header));

			}
		} else if (orderBy != null) {
			headers = dlerOrderHeaderRepo.findByOrderBy(orderBy);
			List<String> orderIds = headers.stream().map(DlerOrderHeader::getOrderId).collect(Collectors.toList());
			if (!orderIds.isEmpty()) {
				orderDetails = dlerOrderDetailsRepo.findByOrderIdIn(orderIds);
	            setDlerIdAndOrderTo(orderDetails, headers);

			}
		} else if (fromDate != null && toDate != null) {
			headers = dlerOrderHeaderRepo.findByOrderDateBetween(LocalDate.parse(fromDate), LocalDate.parse(toDate));
			List<String> orderIds = headers.stream().map(DlerOrderHeader::getOrderId).collect(Collectors.toList());
			if (!orderIds.isEmpty()) {
				orderDetails = dlerOrderDetailsRepo.findByOrderIdIn(orderIds);
	            setDlerIdAndOrderTo(orderDetails, headers);

			}
		} else if (orderTo != null) {
			headers = dlerOrderHeaderRepo.findByOrderTo(orderTo);
			List<String> orderIds = headers.stream().map(DlerOrderHeader::getOrderId).collect(Collectors.toList());
			if (!orderIds.isEmpty()) {
				orderDetails = dlerOrderDetailsRepo.findByOrderIdIn(orderIds);
	            setDlerIdAndOrderTo(orderDetails, headers);

			}
		}
		else {
	        // Fallback: Get all records if no parameters are provided
	        orderDetails = dlerOrderDetailsRepo.findAll();
	    }
		return orderDetails;

	}
	private void setDlerIdAndOrderTo(List<DlerOrderDetails> orderDetails, List<DlerOrderHeader> headers) {
	    for (DlerOrderDetails details : orderDetails) {
	        DlerOrderHeader matchingHeader = headers.stream()
	            .filter(header -> header.getOrderId().equals(details.getOrderId()))
	            .findFirst()
	            .orElse(null);
	        if (matchingHeader != null) {
	            details.setDlerId(matchingHeader.getOrderBy());
	            details.setOrderTo(matchingHeader.getOrderTo());
	        }
	    }
	}

	@Override
	public List<DlerOrderHeader> getOrderHeaderData(String orderId, String orderBy, String fromDate, String toDate,
			String orderTo) {
		List<DlerOrderHeader> headers = new ArrayList<>();

		if (orderId != null) {
			DlerOrderHeader details = dlerOrderHeaderRepo.findByOrderId(orderId);
			if (details != null) {
	            headers.add(details);
	        }
		} else if (orderBy != null) {
			headers = dlerOrderHeaderRepo.findByOrderBy(orderBy);
			
		} else if (fromDate != null && toDate != null) {
			headers = dlerOrderHeaderRepo.findByOrderDateBetween(LocalDate.parse(fromDate), LocalDate.parse(toDate));
			
			
		} else if (orderTo != null) {
			headers = dlerOrderHeaderRepo.findByOrderTo(orderTo);
			
		}
		return headers;

	}
		
	
}
