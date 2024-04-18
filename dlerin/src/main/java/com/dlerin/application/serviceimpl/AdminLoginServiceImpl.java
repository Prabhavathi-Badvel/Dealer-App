package com.dlerin.application.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dlerin.application.dto.AdminLoginDto;
import com.dlerin.application.entity.AdminLogin;
import com.dlerin.application.repository.AdminLoginRepo;
import com.dlerin.application.service.AdminLoginService;

@Service
public class AdminLoginServiceImpl implements AdminLoginService {

	@Autowired
	AdminLoginRepo adminLoginRepo;

	@Autowired
	OtpGenerationServiceImpl otpService;

	@Override
	public AdminLoginDto saveAdminDetails(AdminLogin adminLogin) {
		if (adminLoginRepo.findByEmailIdOrMobileNoOrEmpId(adminLogin.getEmailId(), adminLogin.getMobileNo(),
				adminLogin.getEmpId()) == null) {
			BCryptPasswordEncoder byCrypt = new BCryptPasswordEncoder();
			String encryptPassword = byCrypt.encode(adminLogin.getPassword());
			adminLogin.setPassword(encryptPassword);

			AdminLogin admin = adminLoginRepo.save(adminLogin);

			AdminLoginDto adminLoginDto = new AdminLoginDto();

			adminLoginDto.setAddress(adminLogin.getAddress());
			adminLoginDto.setEmailId(adminLogin.getEmailId());
			adminLoginDto.setEmpId(adminLogin.getEmpId());
			adminLoginDto.setMobileNo(adminLogin.getMobileNo());
			adminLoginDto.setName(adminLogin.getName());
			adminLoginDto.setRegisteredDate(admin.getRegisteredDate());
			adminLoginDto.setRole(adminLogin.getRole());
			adminLoginDto.setUpdatedBy(adminLogin.getUpdatedBy());

			return adminLoginDto;
		}
		return null;
	}

	@Override
	public String loginDetails(String emailId, long mobileNo, String password) {
		BCryptPasswordEncoder byCrypt = new BCryptPasswordEncoder();
		Optional<AdminLogin> loginDb = Optional.ofNullable(adminLoginRepo.findByEmailIdOrMobileNo(emailId, mobileNo));

		if (loginDb.isPresent()) {
			AdminLogin adminLogin = loginDb.get();
			if (byCrypt.matches(password, adminLogin.getPassword())) {
				return "login";
			} else {
				return "InvalidPassword";
			}
		} else {
			return "invalid user";
		}
	}

	@Override
	public AdminLoginDto getLoginDto(String emailId, long mobileNo) {
		Optional<AdminLogin> user = Optional.of(adminLoginRepo.findByEmailIdOrMobileNo(emailId, mobileNo));
		AdminLogin db = user.get();

		AdminLoginDto logindto = new AdminLoginDto();

		logindto.setAddress(db.getAddress());
		logindto.setEmailId(db.getEmailId());
		logindto.setEmpId(db.getEmpId());
		logindto.setMobileNo(db.getMobileNo());
		logindto.setName(db.getName());
		logindto.setRegisteredDate(db.getRegisteredDate());
		logindto.setRole(db.getRole());
		logindto.setUpdatedBy(db.getUpdatedBy());

		return logindto;

	}

	@Override
	public AdminLoginDto getAdminLoginDetails(String emailId, long mobileNo, String empId) {
		Optional<AdminLogin> user = Optional
				.of(adminLoginRepo.findByEmailIdOrMobileNoOrEmpId(emailId, mobileNo, empId));
		AdminLogin dbt = user.get();

		AdminLoginDto logindto = new AdminLoginDto();

		logindto.setAddress(dbt.getAddress());
		logindto.setEmailId(dbt.getEmailId());
		logindto.setEmpId(dbt.getEmpId());
		logindto.setMobileNo(dbt.getMobileNo());
		logindto.setName(dbt.getName());
		logindto.setRegisteredDate(dbt.getRegisteredDate());
		logindto.setRole(dbt.getRole());
		logindto.setUpdatedBy(dbt.getUpdatedBy());

		return logindto;

	}

	@Override
	public String changePassword(String emailId, String oldPassword, String newPassword, String confirmPassword) {
		BCryptPasswordEncoder byCrypt = new BCryptPasswordEncoder();
		Optional<AdminLogin> user = adminLoginRepo.findByEmailId(emailId);
		if (user.isPresent()) {
			if (byCrypt.matches(oldPassword, user.get().getPassword())) {
				if (newPassword.equals(confirmPassword)) {
					String encryptPassword = byCrypt.encode(confirmPassword);
					user.get().setPassword(encryptPassword);
					adminLoginRepo.save(user.get());
					return "changed";
				} else {
					return "notMatched";
				}
			} else {
				return "incorrect";
			}
		} else {
			return "invalid";
		}

	}

	@Override
	public AdminLogin updateprofile(AdminLogin adminLogin, String empId) {

		Optional<AdminLogin> empIdExists = Optional.of(adminLoginRepo.findByEmpId(empId));
		if (empIdExists.isPresent()) {

			AdminLogin adminDb = empIdExists.get();

			adminDb.setAddress(adminLogin.getAddress());
			adminDb.setName(adminLogin.getName());
			adminDb.setRole(adminLogin.getRole());

			return adminLoginRepo.save(adminDb);
		}

		return null;

	}

	@Override
	public String sendMail(String emailId) {
		Optional<AdminLogin> userOp = adminLoginRepo.findByEmailId(emailId);
		if (userOp.isPresent()) {
			otpService.generateOtp(emailId);
			return "otp";
		}
		return null;
	}

	@Override
	public String forgetPassword(String emailId, String otp, String newPassword, String confirmPassword) {
		BCryptPasswordEncoder byCrypt = new BCryptPasswordEncoder();

		Optional<AdminLogin> userOp = adminLoginRepo.findByEmailId(emailId);
		if (userOp.isPresent()) {
			if (otpService.verifyOtp(emailId, otp)) {
				if (newPassword.equals(confirmPassword)) {
					String encryptPassword = byCrypt.encode(confirmPassword);
					userOp.get().setPassword(encryptPassword);
					adminLoginRepo.save(userOp.get());
					return "changed";
				} else {
					return "notMatched";
				}
			} else {
				return "incorrect";
			}
		}
		return null;

	}
}
