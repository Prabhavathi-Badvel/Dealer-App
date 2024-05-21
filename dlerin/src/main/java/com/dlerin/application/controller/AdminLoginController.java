package com.dlerin.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.AdminChangeForgotdto;
import com.dlerin.application.dto.AdminLoginDto;
import com.dlerin.application.dto.AdminLoginDto1;
import com.dlerin.application.dto.LoginDto;
import com.dlerin.application.dto.ResponseAdminLoginDto;
import com.dlerin.application.dto.ResponseAdminLoginDto1;
import com.dlerin.application.dto.ResponseAdminLoginDto2;
import com.dlerin.application.dto.ResponseMessageDto;
import com.dlerin.application.entity.AdminLogin;
import com.dlerin.application.service.AdminLoginService;

import jakarta.annotation.security.PermitAll;

@RestController
@PermitAll
public class AdminLoginController {

	@Autowired
	AdminLoginService adminLoginService;

	ResponseAdminLoginDto response = new ResponseAdminLoginDto();
	
	ResponseAdminLoginDto1 response1 = new ResponseAdminLoginDto1();
	ResponseAdminLoginDto2 response2 = new ResponseAdminLoginDto2();
	
	ResponseMessageDto message = new ResponseMessageDto();
	
	@PostMapping("/dlerin-register-adminlogin")
	public ResponseEntity<?> saveAdminLogin(@RequestBody AdminLogin adminLoginDto) {
		try {

			AdminLoginDto1 savedLogin = adminLoginService.saveAdminDetails(adminLoginDto);
			if (savedLogin != null) {
				
				response.setMessage("Admin registered successfully");
				response.setStatus(true);
				response.setAdminData(savedLogin);
				return new ResponseEntity<>(response, HttpStatus.OK);

			}
			response.setMessage("admin already exists");
			response.setStatus(false);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			response.setMessage("Failed to add");
			response.setStatus(false);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@PostMapping("/dlerin-login-adminlogin")
	public ResponseEntity<?> login(@RequestBody LoginDto login) {
		String userEmail = login.getEmail();
		String userMobile = login.getMobileNo();
		String userPassword = login.getPassword();
		ResponseAdminLoginDto2 response=adminLoginService.loginDetails(userEmail, userMobile, userPassword);
		if(response.getJwtToken()!=null) {
			return new ResponseEntity<ResponseAdminLoginDto2>(response, HttpStatus.OK);
		}
		return new ResponseEntity<ResponseAdminLoginDto2>(response, HttpStatus.UNAUTHORIZED);

	}

	@PreAuthorize("hasAuthority('Admin')")
	@GetMapping("/dlerin-get-adminlogin-profile")
	public ResponseEntity<?> getAdminProfile(@RequestBody AdminLoginDto adLoginDto) {
		String emailId = adLoginDto.getEmailId();
		String mobileNo = adLoginDto.getMobileNo();
		String empId = adLoginDto.getEmpId();
		
		try {
			AdminLoginDto details=adminLoginService.getAdminLoginDetails(emailId, mobileNo, empId);
			if ( details == null) {
				response2.setMessage("Invalid credentials");
				response2.setStatus(false);
				return new ResponseEntity<>(response2, HttpStatus.BAD_REQUEST);
			}
			response2.setMessage("admin details");
			response2.setStatus(true);
			response2.setAdminData(details);
			return new ResponseEntity<>(response2,HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PreAuthorize("hasAuthority('Admin')")
	@PostMapping("/change/AdminPassword")
	public ResponseEntity<?> changeAdminPassword(@RequestBody AdminChangeForgotdto changePassword) {
		String emailId = changePassword.getEmailId();
		String oldPassword = changePassword.getOldPassword();
		String newPassword = changePassword.getNewPassword();
		String confirmPassword = changePassword.getConfirmPassword();
		String mobileNo = changePassword.getMobileNo();

		try {
			if (adminLoginService.changePassword(emailId, oldPassword, newPassword, confirmPassword,mobileNo) == "changed") {
				message.setMessage("Password Changed Successfully");
				message.setStatus(true);
				return new ResponseEntity<>(message, HttpStatus.OK);
			} else if (adminLoginService.changePassword(emailId, oldPassword, newPassword,
					confirmPassword,mobileNo) == "notMatched") {
				message.setMessage("New Passwords Not Matched.!");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			} else if (adminLoginService.changePassword(emailId, oldPassword, newPassword,
					confirmPassword,mobileNo) == "incorrect") {
				message.setMessage("Old Password is Incorrect");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			message.setMessage(e.getMessage());
			message.setStatus(false);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		}
		message.setMessage("Invalid Admin");
		message.setStatus(false);
		return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAuthority('Admin')")
	@PutMapping("/dlerin-update-adminlogin")
	public ResponseEntity<?> updateAdminProfile(@RequestBody AdminLogin adminLogin) {

		String empId = adminLogin.getEmpId();

		AdminLogin update = adminLoginService.updateprofile(adminLogin, empId);
		try {
			if (update == null) {
				response1.setMessage("Invalid Employee id");
				response1.setStatus(false);
				return new ResponseEntity<>(response1, HttpStatus.BAD_REQUEST);

			} else {
				response1.setMessage("Profile updated successfully");
				response1.setStatus(true);
				response1.setUpdatedData(update);
				return ResponseEntity.ok().body(response1);
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping("/admin/forgetPassword/sendOtp")
	public ResponseEntity<?> sendOtpForgotPassword(@RequestBody AdminChangeForgotdto password) {
		String emailId = password.getEmailId();
		String mobileNo= password.getMobileNo();
		try {
			if(emailId!=null&& mobileNo==null) {
				if (adminLoginService.sendMail(emailId) != null) {
					message.setMessage("OTP Sent to Registered EmailId");
					message.setStatus(true);
					return new ResponseEntity<>(message, HttpStatus.OK);
				}
				message.setMessage("Invalid EmailId");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
			}else {
				if (adminLoginService.sendSms(mobileNo) != null) {
					message.setMessage("OTP Sent to Registered Mobile Number");
					message.setStatus(true);
					return new ResponseEntity<>(message, HttpStatus.OK);
				}
				message.setMessage("Invalid Mobile Number");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			message.setMessage("Invalid EmailId");
			message.setStatus(false);
			return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
		}
		

	}

	@PostMapping("/admin/forgetPassword/verifyOtpAndChangePassword")
	public ResponseEntity<?> verifyOtpForgotPassword(@RequestBody AdminChangeForgotdto forgotPwd) {

		String emailId = forgotPwd.getEmailId();
		String otp = forgotPwd.getOtp();
		String newPassword = forgotPwd.getNewPassword();
		String confirmPassword = forgotPwd.getConfirmPassword();
		String mobileNo = forgotPwd.getMobileNo();
		

		try {
			 String data = adminLoginService.forgetPassword(emailId, otp, newPassword, confirmPassword,mobileNo);
			 if (data == "changed") {
				 message.setMessage("Password Changed Successfully");
				 message.setStatus(true);
					return new ResponseEntity<>(message, HttpStatus.OK);
				} else if (data == "notMatched") {
					message.setMessage("New Passwords Not Matched");
					message.setStatus(false);
					return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
				} else if (data== "incorrect") {
					message.setMessage("Invalid OTP");
					message.setStatus(false);
					return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
				}else if (data== "incorrectEmail") {
					message.setMessage("Invalid Email ID");
					message.setStatus(false);
					return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
				}else {
					message.setMessage("Invalid Mobile Number");
					message.setStatus(false);
					return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				message.setMessage(e.getMessage());
				message.setStatus(false);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
			}

	}
}
