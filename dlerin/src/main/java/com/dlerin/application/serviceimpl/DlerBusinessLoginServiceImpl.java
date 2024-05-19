package com.dlerin.application.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dlerin.application.dto.DlerBusinessLoginDto;
import com.dlerin.application.dto.DlerBusinessLoginDto1;
import com.dlerin.application.dto.DlerBusinessLoginDto2;
import com.dlerin.application.dto.ResponseDlerLoginDto;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.securities.JwtService;
import com.dlerin.application.service.DlerBusinessLoginService;

@Service
public class DlerBusinessLoginServiceImpl implements DlerBusinessLoginService {

	@Autowired
	DlerBusinessLoginRepo dlerBusinessLoginRepo;

	@Autowired
	BCryptPasswordEncoder byCrypt;

	@Autowired
	JwtService jwtService;

	@Override
	public DlerBusinessLoginDto addDlerBusinessProfile(DlerBusinessLogin dbl) {
		DlerBusinessLogin existingProfile = dlerBusinessLoginRepo.findByDlerUserIdOrDlerEmailIdOrDlerMobileNo(
				dbl.getDlerUserId(), dbl.getDlerEmailId(), dbl.getDlerMobileNo());
		if (existingProfile == null) {
			BCryptPasswordEncoder byCrypt = new BCryptPasswordEncoder();
			String encryptPassword = byCrypt.encode(dbl.getDlerPassword());
			dbl.setDlerPassword(encryptPassword);

			dbl.setDlerEmailOtp(null);
			dbl.setDlerEmailVerify("no");
			dbl.setDlerMobileOtp(null);
			dbl.setDlerMobileVerify("no");
			dbl.setDlerStatus("active");
			dbl.setUserType("Dealer");

			DlerBusinessLogin saved = dlerBusinessLoginRepo.save(dbl);

			DlerBusinessLoginDto dblDto = new DlerBusinessLoginDto();

			dblDto.setDlerEmailId(dbl.getDlerEmailId());
			dblDto.setDlerMobileNo(dbl.getDlerMobileNo());
			dblDto.setDlerName(dbl.getDlerName());
			dblDto.setDlerRegDate(dbl.getDlerRegDate());
			dblDto.setUserType(dbl.getUserType());
			dblDto.setDlerUserId(dbl.getDlerUserId());

			return dblDto;
		}
		return null;
	}

	@Override
	public String isEmailExists(String dlerEmailId) {

		if (dlerBusinessLoginRepo.findByDlerEmailId(dlerEmailId) != null) {
			return dlerEmailId;
		} else {

		}
		return null;
	}

	@Override
	public DlerBusinessLogin updateData(String otp, String dlerEmailId) {
		Optional<DlerBusinessLogin> existedById = Optional.of(dlerBusinessLoginRepo.findByDlerEmailId(dlerEmailId));

		if (existedById.isPresent()) {

			existedById.get().setDlerEmailOtp(otp);
			existedById.get().setDlerEmailVerify("yes");

			return dlerBusinessLoginRepo.save(existedById.get());
		}
		return null;
	}

	@Override
	public ResponseDlerLoginDto DlerloginDetails(String dlerEmailId, String dlerMobileNo, String dlerPassword) {
		ResponseDlerLoginDto response = new ResponseDlerLoginDto();

		DlerBusinessLogin login = dlerBusinessLoginRepo.findByDlerEmailIdOrDlerMobileNo(dlerEmailId, dlerMobileNo);
		if (login != null && login.getDlerStatus().equalsIgnoreCase("active")) {

			if ((dlerEmailId != null && login.getDlerEmailVerify().equalsIgnoreCase("yes"))
					|| (dlerMobileNo != null && login.getDlerMobileVerify().equalsIgnoreCase("yes"))) {
				if (byCrypt.matches(dlerPassword, login.getDlerPassword())) {

					String jwtToken = jwtService.generateToken(login.getDlerEmailId(), login.getUserType());
					response.setMessage("Login Successful");
					response.setStatus(true);
					response.setJwtToken(jwtToken);
					response.setLoginDetails(getDlerBusinessLoginDto(login.getDlerUserId()));
					return response;
				} else {
					response.setMessage("Invalid Password");
					response.setStatus(false);
				}
			} else {
				response.setMessage("Please verify your email/mobile first");
				response.setStatus(false);
			}
		} else {
			response.setMessage("Invalid User or Inactive User");
			response.setStatus(false);
		}
		return response;
	}

	@Override
	public DlerBusinessLoginDto1 getDlerBusinessLoginDto(String userId) {
		DlerBusinessLoginDto1 loginDto = new DlerBusinessLoginDto1();

		Optional<DlerBusinessLogin> loginOptional = Optional.ofNullable(dlerBusinessLoginRepo.findByDlerUserId(userId));

		loginDto.setDlerUserId(loginOptional.get().getDlerUserId());
		loginDto.setDlerEmailId(loginOptional.get().getDlerEmailId());
		loginDto.setDlerMobileNo(loginOptional.get().getDlerMobileNo());
		loginDto.setDlerName(loginOptional.get().getDlerName());
		loginDto.setDlerUserId(loginOptional.get().getDlerUserId());

		return loginDto;
	}

	@Override
	public DlerBusinessLoginDto2 getBusinessProfile(String dlerEmailId, String dlerMobileNo, String dlerUserId) {

		Optional<DlerBusinessLogin> dp = Optional.ofNullable(dlerBusinessLoginRepo
				.findByDlerUserIdOrDlerEmailIdOrDlerMobileNo(dlerUserId, dlerEmailId, dlerMobileNo));

		if (dp.isPresent()) {
			DlerBusinessLogin loginDb = dp.get();

			DlerBusinessLoginDto2 dto = new DlerBusinessLoginDto2();
			dto.setDlerEmailId(loginDb.getDlerEmailId());
			dto.setDlerEmailOtp(loginDb.getDlerEmailOtp());
			dto.setDlerEmailVerify(loginDb.getDlerEmailVerify());
			dto.setDlerMobileNo(loginDb.getDlerMobileNo());
			dto.setDlerMobileOtp(loginDb.getDlerMobileOtp());
			dto.setDlerMobileVerify(loginDb.getDlerMobileVerify());
			dto.setDlerName(loginDb.getDlerName());
			dto.setDlerPasswordUpdatedDate(loginDb.getDlerPasswordUpdatedDate());
			dto.setDlerRegDate(loginDb.getDlerRegDate());
			dto.setDlerStatus(loginDb.getDlerStatus());
			dto.setDlerStatusUpdatedBy(loginDb.getDlerStatusUpdatedBy());
			dto.setDlerUserId(loginDb.getDlerUserId());
			dto.setUserType(loginDb.getUserType());

			return dto;
		}
		return null;

	}

	@Override
	public DlerBusinessLogin updateDataWithMobile(String dlerMobileNo) {
		Optional<DlerBusinessLogin> existedById = Optional.of(dlerBusinessLoginRepo.findByDlerMobileNo(dlerMobileNo));

		if (existedById.isPresent()) {
			existedById.get().setDlerMobileVerify("yes");
			existedById.get().setDlerStatus("active");
			return dlerBusinessLoginRepo.save(existedById.get());
		}
		return null;
	}

}
