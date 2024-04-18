package com.dlerin.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.AdminChangeForgotdto;
import com.dlerin.application.dto.AdminLoginDto;
import com.dlerin.application.dto.AdminLoginRequestDto;
import com.dlerin.application.dto.ResponseAdminLoginDto;
import com.dlerin.application.dto.ResponseAdminLoginRetunDetailsDto;
import com.dlerin.application.entity.AdminLogin;
import com.dlerin.application.serviceimpl.AdminLoginServiceImpl;

@RestController
public class AdminLoginController {

	@Autowired
	AdminLoginServiceImpl adminLoginServiceImpl;

	@PostMapping("/dlerin-register-adminlogin")
	public ResponseEntity<?> saveAdminLogin(@RequestBody AdminLogin adminLoginDto) {
		try {

			AdminLoginDto savedLogin = adminLoginServiceImpl.saveAdminDetails(adminLoginDto);
			if (savedLogin != null) {
				ResponseAdminLoginDto responseAdminLoginDto = new ResponseAdminLoginDto();
				responseAdminLoginDto.setMessage("Details added successfully");
				responseAdminLoginDto.setAdminData(savedLogin);
				return new ResponseEntity<>(responseAdminLoginDto, HttpStatus.OK);

			}
			return new ResponseEntity<>("user already exists", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			ResponseAdminLoginDto errorResponseDto = new ResponseAdminLoginDto();
			errorResponseDto.setMessage("Failed to add  ");
			return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PostMapping("/dlerin-login-adminlogin")
	public ResponseEntity<?> login(@RequestBody AdminLoginRequestDto login) {
		String emailId = login.getEmailId();
		long mobileNo = login.getMobileNo();
		String password = login.getPassword();
		try {
			if (adminLoginServiceImpl.loginDetails(emailId, mobileNo, password).equals("login")) {
				ResponseAdminLoginRetunDetailsDto rs = new ResponseAdminLoginRetunDetailsDto();
				rs.setMsge("Login successful....");
				rs.setLogData(adminLoginServiceImpl.getLoginDto(emailId, mobileNo));
				return new ResponseEntity<>(rs, HttpStatus.OK);

			} else if (adminLoginServiceImpl.loginDetails(emailId, mobileNo, password).equals("InvalidPassword")) {
				return new ResponseEntity<>("Invalid Password,Please enter correct password", HttpStatus.UNAUTHORIZED);
			} else if (adminLoginServiceImpl.loginDetails(emailId, mobileNo, password).equals("inactive")) {
				return new ResponseEntity<>("Please Verify Email Before Login.", HttpStatus.UNAUTHORIZED);
			}

			return new ResponseEntity<>("Invalid User....!", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping("/dlerin-get-adminlogin-profile")
	public ResponseEntity<?> getAdminProfile(@RequestBody AdminLoginDto adLoginDto) {
		String emailId = adLoginDto.getEmailId();
		long mobileNo = adLoginDto.getMobileNo();
		String empId = adLoginDto.getEmpId();
		try {
			if (adminLoginServiceImpl.getAdminLoginDetails(emailId, mobileNo, empId) == null) {
				return new ResponseEntity<>("Invalid credentials....!", HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<>(adminLoginServiceImpl.getAdminLoginDetails(emailId, mobileNo, empId),
					HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping("/dlerin-change-password")
	public ResponseEntity<String> changeCustomerPassword(@RequestBody AdminChangeForgotdto cfPwd) {

		String emailId = cfPwd.getEmailId();
		String oldPassword = cfPwd.getOldPassword();
		String newPassword = cfPwd.getNewPassword();
		String confirmPassword = cfPwd.getConfirmPassword();

		try {
			if (adminLoginServiceImpl.changePassword(emailId, oldPassword, newPassword, confirmPassword) == "changed") {
				return new ResponseEntity<>("Password Changed Successfully..", HttpStatus.OK);
			} else if (adminLoginServiceImpl.changePassword(emailId, oldPassword, newPassword,
					confirmPassword) == "notMatched") {
				return new ResponseEntity<>("New Passwords Not Matched.!", HttpStatus.BAD_REQUEST);
			} else if (adminLoginServiceImpl.changePassword(emailId, oldPassword, newPassword,
					confirmPassword) == "incorrect") {
				return new ResponseEntity<>("Old Password is Incorrect", HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.getMessage();
			return new ResponseEntity<>("Invalid User.!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>("Invalid User.!", HttpStatus.NOT_FOUND);
	}

	@PutMapping("/dlerin-update-adminlogin")
	public ResponseEntity<?> updateAdminProfile(@RequestBody AdminLogin adminLogin) {

		String empId = adminLogin.getEmpId();

		AdminLogin update = adminLoginServiceImpl.updateprofile(adminLogin, empId);
		try {
			if (update == null) {
				return new ResponseEntity<>("invalid Employee id", HttpStatus.BAD_REQUEST);

			} else {
				String successMessage = "Profile updated successfully";
				return ResponseEntity.ok().body(successMessage);
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping("/dlerin-forget-pwd-send-otp")
	public ResponseEntity<String> sendOtpForPasswordChange(@RequestBody AdminChangeForgotdto cfPwd) {
		String emailId = cfPwd.getEmailId();
		try {
			if (adminLoginServiceImpl.sendMail(emailId) != null) {
				return new ResponseEntity<String>("OTP Sent to Registered EmailId.", HttpStatus.OK);
			}
			return new ResponseEntity<>("Invalid EmailId..!", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}

	}

	@PostMapping("/dlerin-forget-pwd-change")
	public ResponseEntity<String> verifyOtpForPasswordChange(@RequestBody AdminChangeForgotdto cfPwd) {

		String emailId = cfPwd.getEmailId();
		String otp = cfPwd.getOtp();
		String newPassword = cfPwd.getNewPassword();
		String confirmPassword = cfPwd.getConfirmPassword();

		try {
			if (adminLoginServiceImpl.forgetPassword(emailId, otp, newPassword, confirmPassword) == "changed") {
				return new ResponseEntity<>("Password Changed Successfully..", HttpStatus.OK);
			} else if (adminLoginServiceImpl.forgetPassword(emailId, otp, newPassword,
					confirmPassword) == "notMatched") {
				return new ResponseEntity<>("New Passwords Not Matched.!", HttpStatus.BAD_REQUEST);
			} else if (adminLoginServiceImpl.forgetPassword(emailId, otp, newPassword,
					confirmPassword) == "incorrect") {
				return new ResponseEntity<>("Invalid OTP..!", HttpStatus.UNAUTHORIZED);
			}

			return new ResponseEntity<>("Invalid EmailId..!", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}

	}
}
