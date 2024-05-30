package com.dlerin.application.service;

import com.dlerin.application.dto.AdminLoginDto;
import com.dlerin.application.dto.AdminLoginDto1;
import com.dlerin.application.dto.ResponseAdminLoginDto2;
import com.dlerin.application.entity.AdminLogin;

public interface AdminLoginService {

	public AdminLoginDto1 saveAdminDetails(AdminLogin adminLogin);
	public String changePassword(String emailId, String oldPassword, String newPassword, String confirmPassword,String password);
	public ResponseAdminLoginDto2 loginDetails(String emailId, String mobileNo, String password);
	public AdminLogin updateprofile(AdminLogin adminLogin, String empId);
	public String sendMail(String emailId);
	public String sendSms(String mobile);
	public String forgetPassword(String emailId, String otp, String newPassword, String confirmPassword,String mobileNo);
	public AdminLoginDto getAdminLoginDetails(String emailId, String mobileNo, String empId);
}
