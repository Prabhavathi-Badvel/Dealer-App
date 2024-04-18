package com.dlerin.application.service;

import com.dlerin.application.dto.DlerBusinessLoginDto;
import com.dlerin.application.entity.DlerBusinessLogin;

public interface DlerBusinessLoginService {
	public DlerBusinessLoginDto addDlerBusinessProfile(DlerBusinessLogin dblDto);

	public String isEmailExists(String dlerEmailId);

	public DlerBusinessLogin updateData(String otp, String dlerEmailId);

	public String DlerloginDetails(String dlerEmailId, long dlerMobileNo, String dlerPassword);

	public DlerBusinessLoginDto getDlerBusinessLoginDto(String dlerEmailId, long dlerMobileNo);

	public DlerBusinessLoginDto getBusinessProfile(String dlerEmailId, long dlerMobileNo, String dlerUserId);
}
