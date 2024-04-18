package com.dlerin.application.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dlerin.application.dto.DlerBusinessLoginDto;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.service.DlerBusinessLoginService;

@Service
public class DlerBusinessLoginServiceImpl implements DlerBusinessLoginService {

	@Autowired
	DlerBusinessLoginRepo dlerBusinessLoginRepo;

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
			dbl.setDlerMobileVerify("yes");

			DlerBusinessLogin saved = dlerBusinessLoginRepo.save(dbl);

			DlerBusinessLoginDto dblDto = new DlerBusinessLoginDto();

			dblDto.setDlerEmailId(dbl.getDlerEmailId());
			dblDto.setDlerEmailOtp(dbl.getDlerEmailOtp());
			dblDto.setDlerEmailVerify(dbl.getDlerEmailVerify());
			dblDto.setDlerMobileNo(dbl.getDlerMobileNo());
			dblDto.setDlerMobileOtp(dbl.getDlerMobileOtp());
			dblDto.setDlerMobileVerify(dbl.getDlerMobileVerify());
			dblDto.setDlerName(dbl.getDlerName());
			dblDto.setDlerPasswordUpdatedDate(dbl.getDlerPasswordUpdatedDate());
			dblDto.setDlerRegDate(dbl.getDlerRegDate());
			dblDto.setDlerStatus(dbl.getDlerStatus());
			dblDto.setDlerStatusUpdatedBy(dbl.getDlerStatusUpdatedBy());
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
	public String DlerloginDetails(String dlerEmailId, long dlerMobileNo, String dlerPassword) {
		BCryptPasswordEncoder byCrypt = new BCryptPasswordEncoder();

		DlerBusinessLogin dblDblogin = dlerBusinessLoginRepo.findByDlerEmailIdOrDlerMobileNo(dlerEmailId, dlerMobileNo);

		if (dblDblogin != null) {
			String status = dblDblogin.getDlerStatus();

			if (status != null && status.equalsIgnoreCase("active")) {
				if (dlerEmailId != null && dlerMobileNo == 0) {
					if (dblDblogin.getDlerEmailVerify().equalsIgnoreCase("yes")) {
						if (byCrypt.matches(dlerPassword, dblDblogin.getDlerPassword())) {
							return "login";
						} else {
							return "InvalidPassword";
						}
					} else {
						return "verifyEmail";
					}
				} else if (dlerEmailId == null && dlerMobileNo != 0) {
					if (dblDblogin.getDlerMobileVerify().equalsIgnoreCase("yes")) {
						if (byCrypt.matches(dlerPassword, dblDblogin.getDlerPassword())) {
							return "login";
						} else {
							return "InvalidPassword";
						}
					} else {
						return "verifyMobile";
					}
				}
			} else {
				return "inactive";
			}
		} else {
			return "invalid user";
		}
		return null;
	}

	@Override
	public DlerBusinessLoginDto getDlerBusinessLoginDto(String dlerEmailId, long dlerMobileNo) {
		Optional<DlerBusinessLogin> isEmailOrMobileExists = Optional
				.ofNullable(dlerBusinessLoginRepo.findByDlerEmailIdOrDlerMobileNo(dlerEmailId, dlerMobileNo));

		if (isEmailOrMobileExists.isPresent()) {
			DlerBusinessLogin dlerDb = isEmailOrMobileExists.get();

			DlerBusinessLoginDto getDblDto = new DlerBusinessLoginDto();

			getDblDto.setDlerEmailId(dlerDb.getDlerEmailId());
			getDblDto.setDlerEmailOtp(dlerDb.getDlerEmailOtp());
			getDblDto.setDlerEmailVerify(dlerDb.getDlerEmailVerify());
			getDblDto.setDlerMobileNo(dlerDb.getDlerMobileNo());
			getDblDto.setDlerMobileOtp(dlerDb.getDlerMobileOtp());
			getDblDto.setDlerMobileVerify(dlerDb.getDlerMobileVerify());
			getDblDto.setDlerName(dlerDb.getDlerName());
			getDblDto.setDlerPasswordUpdatedDate(dlerDb.getDlerPasswordUpdatedDate());
			getDblDto.setDlerRegDate(dlerDb.getDlerRegDate());
			getDblDto.setDlerStatus(dlerDb.getDlerStatus());
			getDblDto.setDlerStatusUpdatedBy(dlerDb.getDlerStatusUpdatedBy());
			getDblDto.setDlerUserId(dlerDb.getDlerUserId());

			return getDblDto;
		}

		return null;
	}

	@Override
	public DlerBusinessLoginDto getBusinessProfile(String dlerEmailId, long dlerMobileNo, String dlerUserId) {

		Optional<DlerBusinessLogin> dp = Optional.ofNullable(dlerBusinessLoginRepo
				.findByDlerUserIdOrDlerEmailIdOrDlerMobileNo(dlerUserId, dlerEmailId, dlerMobileNo));

		if (dp.isPresent()) {
			DlerBusinessLogin loginDb = dp.get();

			DlerBusinessLoginDto dto = new DlerBusinessLoginDto();
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

			return dto;
		}
		return null;

	}

}
