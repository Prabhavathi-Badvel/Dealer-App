package com.dlerin.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.AdminChangeForgotdto;
import com.dlerin.application.dto.AdminLoginDto;
import com.dlerin.application.dto.LoginAdminDto;
import com.dlerin.application.dto.ResponseAdminLoginDto;
import com.dlerin.application.dto.ResponseAdminLoginDto1;
import com.dlerin.application.dto.ResponseAdminLoginDto2;
import com.dlerin.application.dto.ResponseAdminLoginDto3;
import com.dlerin.application.dto.ResponseMessageDto;
import com.dlerin.application.entity.AdminLogin;
import com.dlerin.application.service.AdminLoginService;

import jakarta.annotation.security.PermitAll;

@RestController
@PermitAll
public class AdminLoginController {

	@Autowired
	AdminLoginService adminLoginService;

	@PostMapping("/dlerin-register-adminlogin")
	public ResponseEntity<?> saveAdminLogin(@RequestBody AdminLogin adminLoginDto) {
		try {
			ResponseAdminLoginDto response = new ResponseAdminLoginDto();
			AdminLoginDto savedLogin = adminLoginService.saveAdminDetails(adminLoginDto);
			if (savedLogin != null) {

				response.setMessage("Admin registered successfully");
				response.setStatus(true);
				response.setAdminData(savedLogin);
				return new ResponseEntity<>(response, HttpStatus.OK);

			}
			response.setMessage("admin already exists");
			response.setStatus(false);
			response.setAdminData(null);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
	}

	@PostMapping("/dlerin-login-adminlogin")
	public ResponseEntity<?> login(@RequestBody LoginAdminDto login) {
		String email = login.getEmail();
		String mobileNo = login.getMobileNo();
		String password = login.getPassword();
		ResponseAdminLoginDto2 response = adminLoginService.loginDetails(email, mobileNo, password);
		if (response.getJwtToken() != null) {
			return new ResponseEntity<ResponseAdminLoginDto2>(response, HttpStatus.OK);
		}
		return new ResponseEntity<ResponseAdminLoginDto2>(response, HttpStatus.OK);

	}

	@PreAuthorize("hasAuthority('Admin')")
	@GetMapping("/dlerin-get-adminlogin-profile")
	public ResponseEntity<?> getAdminProfile(@RequestParam(required = false) String emailId,
			@RequestParam(required = false) String mobileNo, @RequestParam(required = false) String empId) {
		ResponseAdminLoginDto3 response3 = new ResponseAdminLoginDto3();
		try {
			AdminLoginDto details = adminLoginService.getAdminLoginDetails(emailId, mobileNo, empId);
			if (details != null) {
				response3.setMessage("admin details");
				response3.setStatus(true);
				response3.setAdminData(details);
				return new ResponseEntity<>(response3, HttpStatus.OK);

			} else {
				response3.setMessage("Invalid admin/check your credentials");
				response3.setStatus(false);
				response3.setAdminData(null);
				return new ResponseEntity<>(response3, HttpStatus.OK);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
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
		ResponseMessageDto message = new ResponseMessageDto();
		try {
			if (adminLoginService.changePassword(emailId, oldPassword, newPassword, confirmPassword,
					mobileNo) == "changed") {
				message.setMessage("Password Changed Successfully");
				message.setStatus(true);
				return new ResponseEntity<>(message, HttpStatus.OK);
			} else if (adminLoginService.changePassword(emailId, oldPassword, newPassword, confirmPassword,
					mobileNo) == "notMatched") {
				message.setMessage("New Passwords Not Matched.!");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.OK);
			} else if (adminLoginService.changePassword(emailId, oldPassword, newPassword, confirmPassword,
					mobileNo) == "incorrect") {
				message.setMessage("Old Password is Incorrect");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.OK);
			}
		} catch (Exception e) {
			message.setMessage(e.getMessage());
			message.setStatus(false);
			return ResponseEntity.status(HttpStatus.OK).body(message);
		}
		message.setMessage("Invalid Admin");
		message.setStatus(false);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('Admin')")
	@PutMapping("/dlerin-update-adminlogin")
	public ResponseEntity<?> updateAdminProfile(@RequestBody AdminLogin adminLogin) {

		String empId = adminLogin.getEmpId();
		ResponseAdminLoginDto1 response1 = new ResponseAdminLoginDto1();
		AdminLogin update = adminLoginService.updateprofile(adminLogin, empId);
		try {
			if (update == null) {
				response1.setMessage("Invalid Admin employee id");
				response1.setStatus(false);
				return new ResponseEntity<>(response1, HttpStatus.OK);

			} else {
				response1.setMessage("Profile updated successfully");
				response1.setStatus(true);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
	}

	@PostMapping("/admin/forgetPassword/sendOtp")
	public ResponseEntity<?> sendOtpForgotPassword(@RequestBody AdminChangeForgotdto password) {
		String emailId = password.getEmailId();
		String mobileNo = password.getMobileNo();
		ResponseMessageDto message = new ResponseMessageDto();
		try {
			if (emailId != null && mobileNo == null) {
				if (adminLoginService.sendMail(emailId) != null) {
					message.setMessage("OTP Sent to Registered EmailId");
					message.setStatus(true);
					return new ResponseEntity<>(message, HttpStatus.OK);
				}
				message.setMessage("Invalid EmailId");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.OK);
			} else {
				if (adminLoginService.sendSms(mobileNo) != null) {
					message.setMessage("OTP Sent to Registered Mobile Number");
					message.setStatus(true);
					return new ResponseEntity<>(message, HttpStatus.OK);
				}
				message.setMessage("Invalid Mobile Number");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.OK);
			}

		} catch (Exception e) {
			message.setMessage("Invalid EmailId");
			message.setStatus(false);
			return new ResponseEntity<>(message, HttpStatus.OK);
		}

	}

	@PostMapping("/admin/forgetPassword/verifyOtpAndChangePassword")
	public ResponseEntity<?> verifyOtpForgotPassword(@RequestBody AdminChangeForgotdto forgotPwd) {

		String emailId = forgotPwd.getEmailId();
		String otp = forgotPwd.getOtp();
		String newPassword = forgotPwd.getNewPassword();
		String confirmPassword = forgotPwd.getConfirmPassword();
		String mobileNo = forgotPwd.getMobileNo();
		ResponseMessageDto message = new ResponseMessageDto();
		try {
			String data = adminLoginService.forgetPassword(emailId, otp, newPassword, confirmPassword, mobileNo);
			if (data == "changed") {
				message.setMessage("Password Changed Successfully");
				message.setStatus(true);
				return new ResponseEntity<>(message, HttpStatus.OK);
			} else if (data == "notMatched") {
				message.setMessage("New Passwords Not Matched");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.OK);
			} else if (data == "incorrect") {
				message.setMessage("Invalid OTP");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.OK);
			} else if (data == "incorrectEmail") {
				message.setMessage("Invalid Email ID");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.OK);
			} else {
				message.setMessage("Invalid Mobile Number");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.OK);
			}
		} catch (Exception e) {
			message.setMessage(e.getMessage());
			message.setStatus(false);
			return ResponseEntity.status(HttpStatus.OK).body(message);
		}

	}
}
