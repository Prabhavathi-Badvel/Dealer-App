package com.dlerin.application.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.dlerin.application.dto.DlerBusinessLoginDto;
import com.dlerin.application.dto.ResponseDlerBusinessLoginAddDto;
import com.dlerin.application.dto.ResponseDlerBusinessLoginGetDto;
import com.dlerin.application.dto.ResponseDlerBusinessLoginLoginDto;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.serviceimpl.DlerBusinessLoginServiceImpl;
import com.dlerin.application.serviceimpl.OtpGenerationServiceImpl;

@RestController
public class DlerBusinessLoginController {

	@Autowired
	DlerBusinessLoginServiceImpl dlerBusinessLoginServiceImpl;

	@Autowired
	OtpGenerationServiceImpl otpService;

	@Autowired
	DlerBusinessLoginRepo dlerBusinessLoginRepo;

	@PostMapping("/dlerin-add-dlerbusinesslogin-details")
	public ResponseEntity<?> addDlerBusinessLogicProfile(@RequestBody DlerBusinessLogin dlerBusinessl) {

		try {
			DlerBusinessLoginDto dblDTO = dlerBusinessLoginServiceImpl.addDlerBusinessProfile(dlerBusinessl);
			if (dblDTO != null) {
				ResponseDlerBusinessLoginAddDto rsDBLDto = new ResponseDlerBusinessLoginAddDto();
				rsDBLDto.setMessage("Dler added successfully");
				rsDBLDto.setDlerProfile(dblDTO);
				return new ResponseEntity<>(rsDBLDto, HttpStatus.OK);
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return new ResponseEntity<>("profile already exists", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/dlerin-send-otp-verify-email")
	public ResponseEntity<String> sendEmail(@RequestBody DlerBusinessLogin login) {
		String dlerEmailId = login.getDlerEmailId();
		String dlerEmailOtp = login.getDlerEmailOtp();
		try {
			if (dlerBusinessLoginServiceImpl.isEmailExists(dlerEmailId) == null) {
				return new ResponseEntity<String>("Invalid EmailId..!", HttpStatus.NOT_FOUND);
			} else {
				Optional<DlerBusinessLogin> user = Optional.of(dlerBusinessLoginRepo.findByDlerEmailId(dlerEmailId));

				if (!user.get().getDlerEmailVerify().equals("yes")) {
					otpService.generateOtp(dlerEmailId);
					return new ResponseEntity<String>("Otp sent to the registered EmailId.", HttpStatus.OK);
				}
				return new ResponseEntity<String>("Email already verified.", HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping("/dlerin-verify-otp-verify-email")
	public ResponseEntity<String> verifyUserEmail(@RequestBody DlerBusinessLogin login) {
		String dlerEmailId = login.getDlerEmailId();
		String dlerEmailOtp = login.getDlerEmailOtp();
		try {
			if (otpService.verifyOtp(dlerEmailId, dlerEmailOtp)) {

				dlerBusinessLoginServiceImpl.updateData(dlerEmailOtp, dlerEmailId);
				return new ResponseEntity<>(" Email Verified successful", HttpStatus.OK);

			}
			return new ResponseEntity<String>("Incorrect OTP, Please enter correct Otp", HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping("/dlerin-login-dlerbusinesslogin")
	public ResponseEntity<?> login(@RequestBody DlerBusinessLogin dblDtoLogin) {
		String dlerEmailId = dblDtoLogin.getDlerEmailId();
		long dlerMobileNo = dblDtoLogin.getDlerMobileNo();
		String dlerPassword = dblDtoLogin.getDlerPassword();

		String dblUser = dlerBusinessLoginServiceImpl.DlerloginDetails(dlerEmailId, dlerMobileNo, dlerPassword);
		try {
			if (dblUser == "login") {
				ResponseDlerBusinessLoginLoginDto rl = new ResponseDlerBusinessLoginLoginDto();
				rl.setMessage(" Dler login successfully");
				rl.setLoginData(dlerBusinessLoginServiceImpl.getDlerBusinessLoginDto(dlerEmailId, dlerMobileNo));
				return new ResponseEntity<>(rl, HttpStatus.OK);

			} else if (dblUser == "InvalidPassword") {
				return new ResponseEntity<>("Invalid Password,Please enter correct password", HttpStatus.UNAUTHORIZED);
			} else if (dblUser == "verifyEmail") {
				return new ResponseEntity<>("Please Verify Email Before Login.", HttpStatus.UNAUTHORIZED);
			} else if (dblUser == "verifyMobile") {
				return new ResponseEntity<>("Please Verify MobileNumber Before Login.", HttpStatus.UNAUTHORIZED);
			} else if (dblUser == "inactive") {
				return new ResponseEntity<>("Admin blocked you, Please contact admin once.", HttpStatus.UNAUTHORIZED);
			} else {
				return new ResponseEntity<>("Invalid User....!", HttpStatus.BAD_REQUEST);

			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping("/dlerin-get-dlerbusinesslogin")
	public ResponseEntity<?> getDlerBusinessProfile(@RequestBody DlerBusinessLoginDto dbl) {

		String dlerEmailId = dbl.getDlerEmailId();
		long dlerMobileNo = dbl.getDlerMobileNo();
		String dlerUserId = dbl.getDlerUserId();

		DlerBusinessLoginDto dlb = dlerBusinessLoginServiceImpl.getBusinessProfile(dlerEmailId, dlerMobileNo,
				dlerUserId);
		try {
			if (dlb != null) {
				ResponseDlerBusinessLoginGetDto rsDB = new ResponseDlerBusinessLoginGetDto();
				rsDB.setMessage("Dler details");
				rsDB.setGetDlerProfile(dlb);
				return new ResponseEntity<>(rsDB, HttpStatus.OK);
			}
			return new ResponseEntity<>("Invalid user....!", HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}
