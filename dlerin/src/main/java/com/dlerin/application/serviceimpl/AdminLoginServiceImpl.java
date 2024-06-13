package com.dlerin.application.serviceimpl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.dlerin.application.dto.AdminLoginDto;
import com.dlerin.application.dto.ResponseAdminLoginDto2;
import com.dlerin.application.entity.AdminLogin;
import com.dlerin.application.repository.AdminLoginRepo;
import com.dlerin.application.securities.JwtService;
import com.dlerin.application.service.AdminLoginService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class AdminLoginServiceImpl implements AdminLoginService {

	@Autowired
	AdminLoginRepo adminLoginRepo;

	@Autowired
	OtpGenerationServiceImpl otpService;

	@Autowired
	JwtService jwtService;

	@Autowired
	BCryptPasswordEncoder byCrypt;

	@PersistenceContext
	private EntityManager entityManager;

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
			adminLoginDto.setUserType(adminLogin.getUserType());
			adminLoginDto.setUpdatedBy(adminLogin.getUpdatedBy());

			return adminLoginDto;
		}
		return null;
	}

	@Override
	public ResponseAdminLoginDto2 loginDetails(String emailId, String mobileNo, String password) {
		ResponseAdminLoginDto2 response = new ResponseAdminLoginDto2();
		Optional<AdminLogin> adminLogin = adminLoginRepo.findByEmailIdOrMobileNo(emailId, mobileNo);
		
		if (adminLogin.isPresent()) {
			
			if (byCrypt.matches(password, adminLogin.get().getPassword())) {
				String jwtToken = jwtService.generateToken(adminLogin.get().getEmailId(), adminLogin.get().getUserType()); 																																														
				response.setMessage("Login Successful");
				response.setStatus(true);
				response.setJwtToken(jwtToken);
				response.setAdminData(getAdminData(adminLogin.get().getEmpId())); 
				return response;
			} else {
				response.setMessage("Invalid Password");
				response.setStatus(false);
			}
		} else {
			response.setMessage("Invalid admin");
			response.setStatus(false);
		}
		return response;
	}

	private AdminLoginDto getAdminData(String empId) {
		AdminLoginDto loginDto = new AdminLoginDto();
		Optional<AdminLogin> login = Optional.ofNullable(adminLoginRepo.findByEmpId(empId));

		loginDto.setName(login.get().getName());
		loginDto.setEmpId(login.get().getEmpId());
		loginDto.setEmailId(login.get().getEmailId());
		loginDto.setMobileNo(login.get().getMobileNo());
		loginDto.setAddress(login.get().getAddress());
		loginDto.setRegisteredDate(login.get().getRegisteredDate());
		loginDto.setUpdatedBy(login.get().getUpdatedBy());
		loginDto.setUserType(login.get().getUserType());

		return loginDto;
	}


	@Override
	public AdminLoginDto getAdminLoginDetails(String emailId, String mobileNo, String empId) {
		Optional<AdminLogin> user = Optional.ofNullable(adminLoginRepo.findByEmailIdOrMobileNoOrEmpId(emailId, mobileNo, empId));

		if (user.isPresent()) {
			AdminLogin dbt = user.get();
			AdminLoginDto logindto = new AdminLoginDto();

			logindto.setAddress(dbt.getAddress());
			logindto.setEmailId(dbt.getEmailId());
			logindto.setEmpId(dbt.getEmpId());
			logindto.setMobileNo(dbt.getMobileNo());
			logindto.setName(dbt.getName());
			logindto.setRegisteredDate(dbt.getRegisteredDate());
			logindto.setUpdatedBy(dbt.getUpdatedBy());
			logindto.setUserType(dbt.getUserType());

			return logindto;
		} else {
			return null;
		}
	}

	@Override
	public String changePassword(String emailId, String oldPassword, String newPassword, String confirmPassword,
			String mobile) {

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
			adminDb.setUserType(adminLogin.getUserType());

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
	public String sendSms(String mobile) {
		Optional<AdminLogin> userOp = Optional.ofNullable(adminLoginRepo.findByMobileNo(mobile));
		if (userOp.isPresent()) {
			otpService.generateMobileOtp(mobile);
			return "otp";
		}
		return null;
	}

	@Override
	public String forgetPassword(String emailId, String otp, String newPassword, String confirmPassword,
			String mobileNo) {
		BCryptPasswordEncoder byCrypt = new BCryptPasswordEncoder();

		Optional<AdminLogin> userEmail = adminLoginRepo.findByEmailId(emailId);
		Optional<AdminLogin> userMobile = Optional.ofNullable(adminLoginRepo.findByMobileNo(mobileNo));
		if (userEmail.isPresent()) {
			if (otpService.verifyOtp(emailId, otp)) {
				if (newPassword.equals(confirmPassword)) {
					String encryptPassword = byCrypt.encode(confirmPassword);
					userEmail.get().setPassword(encryptPassword);
					adminLoginRepo.save(userEmail.get());
					return "changed";
				} else {
					return "notMatched";
				}
			} else {
				return "incorrect";
			}
		} else if (userMobile.isPresent()) {
			if (otpService.verifyMobileOtp(mobileNo, otp)) {
				if (newPassword.equals(confirmPassword)) {
					String encryptPassword = byCrypt.encode(confirmPassword);
					userMobile.get().setPassword(encryptPassword);
					adminLoginRepo.save(userMobile.get());
					return "changed";
				} else {
					return "notMatched";
				}
			} else {
				return "incorrect";
			}
		} else if (!userEmail.isPresent() && userMobile.isPresent()) {
			return "incorrectEmail";
		}
		return null;

	}
}
