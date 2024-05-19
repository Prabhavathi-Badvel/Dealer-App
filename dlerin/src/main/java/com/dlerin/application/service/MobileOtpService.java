package com.dlerin.application.service;

import com.dlerin.application.entity.DlerBusinessLogin;

public interface MobileOtpService {
	public DlerBusinessLogin updateData(String Otp, String dlerMobileNo);
}
