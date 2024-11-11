package com.dlerin.application.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.AdminStoreVerification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl {

	@Autowired
	private JavaMailSender mailsender;

//	public void sendMail(String toMail, String subject, String body) {
//
//		try {
//			MimeMessage message = mailsender.createMimeMessage();
//			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//			helper.setFrom("no_reply@kosuriers.com"); // Update this if needed
//			helper.setTo(toMail);
//			helper.setSubject(subject);
//			helper.setText(body, true); // Set to true for HTML content
//
//			mailsender.send(message);
//			log.info("Email sent successfully to {}", toMail);
//		} catch (MessagingException e) {
//			log.error("Failed to send email to {}: {}", toMail, e.getMessage());
//		}
//	}
//	
//	public void sendMail(String toMail, String otp) {
//		try {
//			MimeMessage message = mailsender.createMimeMessage();
//			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//			helper.setFrom("no_reply@kosuriers.com"); // Update this if needed
//			helper.setTo(toMail);
//			helper.setSubject("YOUR OTP FOR VERIFICATION.");
//			String body = "Thanks for registering with us. Your OTP to verify your email is " + otp + " - www.mrmason.in";
//			helper.setText(body, true); // Set to true for HTML content
//
//			mailsender.send(message);
//			log.info("Email sent successfully to {}", toMail);
//		} catch (MessagingException e) {
//			log.error("Failed to send email to {}: {}", toMail, e.getMessage());
//		}
//	}

	public void sendMail(String toMail, String otp, String operation) {
	    try {
	        MimeMessage message = mailsender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        helper.setFrom("no_reply@kosuriers.com");
	        helper.setTo(toMail);
	        helper.setSubject("YOUR OTP FOR VERIFICATION.");

	        // Customize the message based on operation type
	        String body;
	        if ("registration".equals(operation)) {
	            body = "Thanks for registering with us. Your OTP to verify your email is " + otp + " - http://dev.dler.in";
	        } else if ("forgotPassword".equals(operation)) {
	            body = "We received a request to reset your password. Your OTP for password reset is " + otp + " - http://dev.dler.in";
	        } else {
	            body = "Your OTP is " + otp + " - http://dev.dler.in";
	        }

	        helper.setText(body, true); // Set to true for HTML content
	        mailsender.send(message);
	        log.info("Email sent successfully to {}", toMail);
	    } catch (MessagingException e) {
	        log.error("Failed to send email to {}: {}", toMail, e.getMessage());
	    }
	}
	
	public void sendAdminStoreToMail(String toMail, String otp, String operation) {
	    try {
	        MimeMessage message = mailsender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        helper.setFrom("no_reply@kosuriers.com");
	        helper.setTo(toMail);
	        helper.setSubject("Admin store verificatin status.");
	        // Customize the message based on operation type
	        String body = "Admin store verificatin status is Verified" ;
			helper.setText(body, true); // Set to true for HTML content

			mailsender.send(message);
			log.info("Email sent successfully to {}", toMail);
		} catch (MessagingException e) {
			log.error("Failed to send email to {}: {}", toMail, e.getMessage());
		}
	}
	
	public void sendWebMail(String toMail, String body) {

		MimeMessage message = mailsender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

		try {
			helper.setTo(toMail);
			helper.setSubject("OTP LOGIN SUCCESSFUL");
			helper.setText(body, true);
			mailsender.send(message);
		} catch (MessagingException e) {

		}
	}
}
