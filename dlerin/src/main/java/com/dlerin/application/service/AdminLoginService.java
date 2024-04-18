package com.dlerin.application.service;

import com.dlerin.application.dto.AdminLoginDto;
import com.dlerin.application.entity.AdminLogin;

public interface AdminLoginService {

	public AdminLoginDto saveAdminDetails(AdminLogin adminLogin);

	public String changePassword(String emailId, String oldPassword, String newPassword, String confirmPassword);

	public String loginDetails(String emailId, long mobileNo, String password);

	public AdminLoginDto getLoginDto(String emailId, long mobileNo);

	public AdminLoginDto getAdminLoginDetails(String emailId, long mobileNo, String empId);

	public AdminLogin updateprofile(AdminLogin adminLogin, String empId);

	public String sendMail(String emailId);

	public String forgetPassword(String emailId, String otp, String newPassword, String confirmPassword);
}
