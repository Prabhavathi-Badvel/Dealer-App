package com.dlerin.application.controller;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dlerin.application.dto.ChangeForgotdto;
import com.dlerin.application.dto.DlerBusinessLoginDto;
import com.dlerin.application.dto.DlerBusinessLoginDto2;
import com.dlerin.application.dto.LoginDto;
import com.dlerin.application.dto.ResponseDlerBusinessLoginDto;
import com.dlerin.application.dto.ResponseDlerBusinessLoginDto1;
import com.dlerin.application.dto.ResponseDlerLoginDto;
import com.dlerin.application.dto.ResponseMessageDto;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.service.DlerBusinessLoginService;
import com.dlerin.application.service.MobileOtpService;
import com.dlerin.application.serviceimpl.OtpGenerationServiceImpl;

@RestController
public class DlerBusinessLoginController {

	@Autowired
	DlerBusinessLoginService dlerBusinessLoginService;

	@Autowired
	OtpGenerationServiceImpl otpService;

	@Autowired
	DlerBusinessLoginRepo dlerBusinessLoginRepo;

	@Autowired
	MobileOtpService MobileOtpService;

	ResponseDlerBusinessLoginDto response = new ResponseDlerBusinessLoginDto();

	ResponseDlerBusinessLoginDto1 response1 = new ResponseDlerBusinessLoginDto1();

	ResponseMessageDto message = new ResponseMessageDto();

	@PostMapping("/dlerin-registration")
	public ResponseEntity<?> addDlerBusinessLoginProfile(@RequestBody DlerBusinessLogin dlerBusinessl) {

		try {
			DlerBusinessLoginDto dblDTO = dlerBusinessLoginService.addDlerBusinessProfile(dlerBusinessl);
			if (dblDTO != null) {

				response.setMessage("Dler added successfully");
				response.setStatus(true);
				response.setDlerProfile(dblDTO);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("profile already exists");
				response.setStatus(false);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		} catch (Exception e) {
			response.setMessage("failed to add");
			response.setStatus(false);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

	}

	@PostMapping("/dlerin-send-otp-verify-email")
	public ResponseEntity<?> sendEmail(@RequestBody DlerBusinessLogin login) {
		String dlerEmailId = login.getDlerEmailId();

		try {
			if (dlerBusinessLoginService.isEmailExists(dlerEmailId) == null) {
				message.setMessage("Invalid EmailId");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.OK);
			} else {
				Optional<DlerBusinessLogin> user = Optional.of(dlerBusinessLoginRepo.findByDlerEmailId(dlerEmailId));

				if (!user.get().getDlerEmailVerify().equals("yes")) {
					otpService.generateOtp(dlerEmailId);
					message.setMessage("OTP Sent to Registered EmailId");
					message.setStatus(true);
					return new ResponseEntity<>(message, HttpStatus.OK);
				}
				message.setMessage("Email already verified");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.OK);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
	}

	@PostMapping("/dlerin-verify-otp-verify-email")
	public ResponseEntity<?> verifyUserEmail(@RequestBody DlerBusinessLogin login) {
		String dlerEmailId = login.getDlerEmailId();
		String dlerEmailOtp = login.getDlerEmailOtp();
		try {
			if (otpService.verifyOtp(dlerEmailId, dlerEmailOtp)) {

				dlerBusinessLoginService.updateData(dlerEmailOtp, dlerEmailId);
				message.setStatus(true);
				message.setMessage("Email Verified successful");
				return new ResponseEntity<>(message, HttpStatus.OK);
			}
			message.setMessage("Incorrect OTP, Please enter correct Otp");
			return new ResponseEntity<>(message, HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
	}

	@PostMapping("/dlerin-login-dlerbusinesslogin")
	public ResponseEntity<ResponseDlerLoginDto> login(@RequestBody LoginDto requestDto) {
		String dlrEmail = requestDto.getEmail();
		String dlrMobileNo = requestDto.getMobileNo();
		String dlrPassword = requestDto.getPassword();
		ResponseDlerLoginDto response = dlerBusinessLoginService.DlerloginDetails(dlrEmail, dlrMobileNo, dlrPassword);
		try {
			if (response.getJwtToken() != null) {
				return new ResponseEntity<ResponseDlerLoginDto>(response, HttpStatus.OK);
			}

		} catch (Exception e) {
			e.getMessage();
		}
		return new ResponseEntity<ResponseDlerLoginDto>(response, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('Dealer')")
	@GetMapping("/dlerin-get-dlerbusinesslogin")
	public ResponseEntity<?> getDlerBusinessProfile(@RequestParam(required = false) String dlerEmailId,
			@RequestParam(required = false) String dlerMobileNo, @RequestParam(required = false) String dlerUserId) {

		DlerBusinessLoginDto2 profile = dlerBusinessLoginService.getBusinessProfile(dlerEmailId, dlerMobileNo,
				dlerUserId);
		try {
			if (profile != null) {
				response1.setMessage("Dler details");
				response1.setStatus(true);
				response1.setGetDlerProfile(profile);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			} else {
				response1.setMessage("Invalid dler/check your credentials");
				response1.setStatus(false);
				response1.setGetDlerProfile(profile);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
	}

	@PostMapping("/sendmobileOtp")
	public ResponseEntity<?> sendMobileOtp(@RequestBody DlerBusinessLogin login) {
		String dlerMobileNo = login.getDlerMobileNo();
		try {
			if (dlerBusinessLoginRepo.findByDlerMobileNo(dlerMobileNo) == null) {
				message.setMessage("Invalid mobile number");
				return new ResponseEntity<>(message, HttpStatus.OK);
			} else {
				Optional<DlerBusinessLogin> user = Optional.of(dlerBusinessLoginRepo.findByDlerMobileNo(dlerMobileNo));

				if (user.get().getDlerMobileOtp() == null) {
					otpService.generateMobileOtp(dlerMobileNo);
					message.setStatus(true);
					message.setMessage("Otp sent to the registered mobile number");
					return new ResponseEntity<>(message, HttpStatus.OK);
				}
				message.setMessage("Mobile number already verified");
				return new ResponseEntity<>(message, HttpStatus.OK);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
	}

	@PostMapping("/verifySmsOtp")
	public ResponseEntity<?> verifyMobileOtp(@RequestBody DlerBusinessLogin login) {
		String mobile = login.getDlerMobileNo();
		String otp = login.getDlerMobileOtp();
		try {
			if (otpService.verifyMobileOtp(mobile, otp)) {

				MobileOtpService.updateData(otp, mobile);
				message.setStatus(true);
				message.setMessage("mobile Verified successful");
				return new ResponseEntity<>(message, HttpStatus.OK);

			}
			message.setMessage("Incorrect OTP, Please enter correct Otp");
			return new ResponseEntity<>(message, HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
	}

	@PreAuthorize("hasAuthority('Dealer')")
	@PostMapping("/changeDlerPassword")
	public ResponseEntity<?> changeDlerPassword(@RequestBody ChangeForgotdto changePassword) {
		String dlerEmailId = changePassword.getDlerEmailId();
		String oldPassword = changePassword.getOldPassword();
		String newPassword = changePassword.getNewPassword();
		String confirmPassword = changePassword.getConfirmPassword();
		String dlerMobileNo = changePassword.getDlerMobileNo();

		try {
			String password = dlerBusinessLoginService.changePassword(dlerEmailId, oldPassword, newPassword,
					confirmPassword, dlerMobileNo);
			if (password == "changed") {
				message.setMessage("Password Changed Successfully");
				message.setStatus(true);
				return new ResponseEntity<>(message, HttpStatus.OK);
			} else if (password == "notMatched") {
				message.setMessage("New Passwords Not Matched");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.OK);
			} else if (password == "incorrect") {
				message.setMessage("Old Password is Incorrect");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.OK);
			}
		} catch (Exception e) {
			message.setMessage(e.getMessage());
			message.setStatus(false);
			return ResponseEntity.status(HttpStatus.OK).body(message);
		}
		message.setMessage("Invalid dler");
		message.setStatus(false);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@PostMapping("/dlerforgetPassword/sendOtp")
	public ResponseEntity<?> sendOtpForgotPassword(@RequestBody ChangeForgotdto password) {
		String dlerEmailId = password.getDlerEmailId();
		String dlerMobileNo = password.getDlerMobileNo();
		try {
			if (dlerEmailId != null && dlerMobileNo == null) {
				if (dlerBusinessLoginService.sendMail(dlerEmailId) != null) {
					message.setMessage("OTP Sent to Registered EmailId");
					message.setStatus(true);
					return new ResponseEntity<>(message, HttpStatus.OK);
				}
				message.setMessage("Invalid EmailId");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.OK);
			} else {
				if (dlerBusinessLoginService.sendSms(dlerMobileNo) != null) {
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

	@PostMapping("/dler/forgetPassword/verification")
	public ResponseEntity<?> dlerForgotPasswordVerify(@RequestBody ChangeForgotdto forgotPwd) {
		String dlerEmailId = forgotPwd.getDlerEmailId();
		String otp = forgotPwd.getOtp();
		String newPassword = forgotPwd.getNewPassword();
		String confirmPassword = forgotPwd.getConfirmPassword();
		String mobile = forgotPwd.getDlerMobileNo();

		try {
			String data = dlerBusinessLoginService.forgetPassword(dlerEmailId, otp, newPassword, confirmPassword,
					mobile);
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