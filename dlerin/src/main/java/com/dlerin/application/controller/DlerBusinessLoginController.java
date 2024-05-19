package com.dlerin.application.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.getMessage();
		}
		response.setMessage("failed to add");
		response.setStatus(false);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/dlerin-send-otp-verify-email")
	public ResponseEntity<?> sendEmail(@RequestBody DlerBusinessLogin login) {
		String dlerEmailId = login.getDlerEmailId();

		try {
			if (dlerBusinessLoginService.isEmailExists(dlerEmailId) == null) {
				message.setMessage("Invalid EmailId..!");
				message.setStatus(false);
				return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
			} else {
				Optional<DlerBusinessLogin> user = Optional.of(dlerBusinessLoginRepo.findByDlerEmailId(dlerEmailId));

				if (!user.get().getDlerEmailVerify().equals("yes")) {
					otpService.generateOtp(dlerEmailId);
					message.setMessage("OTP Sent to Registered EmailId.");
					message.setStatus(true);
					return new ResponseEntity<>(message, HttpStatus.OK);
				}
				message.setMessage("Email already verified.");
				message.setStatus(false);
				return new ResponseEntity<>(response, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
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
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
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
		return new ResponseEntity<ResponseDlerLoginDto>(response, HttpStatus.UNAUTHORIZED);
	}

	@PreAuthorize("hasAuthority('Dealer')")
	@GetMapping("/dlerin-get-dlerbusinesslogin")
	public ResponseEntity<?> getDlerBusinessProfile(@RequestBody DlerBusinessLoginDto dbl) {

		String dlerEmailId = dbl.getDlerEmailId();
		String dlerMobileNo = dbl.getDlerMobileNo();
		String dlerUserId = dbl.getDlerUserId();

		DlerBusinessLoginDto2 dlb = dlerBusinessLoginService.getBusinessProfile(dlerEmailId, dlerMobileNo,
				dlerUserId);
		try {
			if (dlb != null) {
				
				response1.setMessage("Dler details");
				response1.setStatus(true);
				response1.setGetDlerProfile(dlb);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			}
			response1.setMessage("Invalid user....!");
			response1.setStatus(false);
			return new ResponseEntity<>(response1, HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping("/sendmobileOtp")
	public ResponseEntity<?> sendMobileOtp(@RequestBody DlerBusinessLogin login) {
		String dlerMobileNo = login.getDlerMobileNo();
		try {
			if (dlerBusinessLoginRepo.findByDlerMobileNo(dlerMobileNo) == null) {
				message.setMessage("Invalid mobile number..!");
				return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
			} else {
				Optional<DlerBusinessLogin> user = Optional.of(dlerBusinessLoginRepo.findByDlerMobileNo(dlerMobileNo));

				if (user.get().getDlerMobileOtp() == null) {
					otpService.generateMobileOtp(dlerMobileNo);
					message.setStatus(true);
					message.setMessage("Otp sent to the registered mobile number.");
					return new ResponseEntity<>(message, HttpStatus.OK);
				}
				message.setMessage("Mobile number already verified.");
				return new ResponseEntity<>(message, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
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
				message.setMessage(" Email Verified successful");
				return new ResponseEntity<>(message, HttpStatus.OK);

			}
			message.setMessage("Incorrect OTP, Please enter correct Otp");
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}