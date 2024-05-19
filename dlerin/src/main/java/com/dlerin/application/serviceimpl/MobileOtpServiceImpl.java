package com.dlerin.application.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.service.MobileOtpService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MobileOtpServiceImpl implements MobileOtpService {
	
	@Autowired
	DlerBusinessLoginRepo dlerBusinessLoginRepo;
	
	@Autowired
	DlerBusinessLoginServiceImpl dlerBusinessLoginServiceImpl;
	
	@Override
	public DlerBusinessLogin updateData(String Otp, String dlerMobileNo) {
		Optional<DlerBusinessLogin> existedById = Optional.of(dlerBusinessLoginRepo.findByDlerMobileNo(dlerMobileNo));
		if(existedById.isPresent()) {
			existedById.get().setDlerMobileOtp(Otp);
			dlerBusinessLoginServiceImpl.updateDataWithMobile(dlerMobileNo);
			return dlerBusinessLoginRepo.save(existedById.get());
		}
		return null;
	}

}
