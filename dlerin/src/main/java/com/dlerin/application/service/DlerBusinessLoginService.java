package com.dlerin.application.service;

import com.dlerin.application.dto.DlerBusinessLoginDto;
import com.dlerin.application.dto.DlerBusinessLoginDto1;
import com.dlerin.application.dto.DlerBusinessLoginDto2;
import com.dlerin.application.dto.ResponseDlerLoginDto;
import com.dlerin.application.entity.DlerBusinessLogin;

public interface DlerBusinessLoginService {

	public DlerBusinessLoginDto addDlerBusinessProfile(DlerBusinessLogin dbl);
	public DlerBusinessLoginDto2 getBusinessProfile(String dlerEmailId, String dlerMobileNo, String dlerUserId);
	public String isEmailExists(String dlerEmailId);
	public DlerBusinessLogin updateData(String otp, String dlerEmailId);
	public ResponseDlerLoginDto DlerloginDetails(String dlerEmailId, String dlerMobileNo, String dlerPassword);
	public DlerBusinessLoginDto1 getDlerBusinessLoginDto(String userId);
	public DlerBusinessLogin updateDataWithMobile(String dlerMobileNo);
}
