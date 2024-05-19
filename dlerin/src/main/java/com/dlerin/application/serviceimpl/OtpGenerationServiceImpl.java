package com.dlerin.application.serviceimpl;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class OtpGenerationServiceImpl {

	@Autowired
	EmailServiceImpl mailService;
	
	@Autowired
	SmsService smsService;
	
	LocalTime local=LocalTime.now();
	
	private final Map<String, String> otpStorage = new HashMap<>(); 

   
    public String generateOtp(String mail) {
    	int randomNum = (int) (Math.random() * 900000) + 100000;
		String otp = String.valueOf(randomNum);
        otpStorage.put(mail, otp);
        mailService.sendMail(mail,otp);
        
        return otp;
    }
    public boolean verifyOtp(String email, String enteredOtp) {
        String storedOtp = otpStorage.get(email);
        return storedOtp != null && storedOtp.equals(enteredOtp);
    }

    
    
	public String generateMobileOtp(String mobile) {
		int randomNum = (int) (Math.random() * 900000) + 100000;
		String otp = String.valueOf(randomNum);
		otpStorage.put(mobile, otp);
		smsService.sendSMSMessage(mobile, otp);
		return otp;
	}
	
	public boolean verifyMobileOtp(String mobile, String enteredOtp) {
		String storedOtp = otpStorage.get(mobile);
		return storedOtp != null && storedOtp.equals(enteredOtp);
	}
}
