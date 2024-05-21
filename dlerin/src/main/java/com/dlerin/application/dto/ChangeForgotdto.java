package com.dlerin.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeForgotdto {
	private String dlerEmailId;
	private String dlerMobileNo;
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
	private String otp;
}
