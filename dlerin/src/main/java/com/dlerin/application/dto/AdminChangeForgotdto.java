package com.dlerin.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminChangeForgotdto {

	String emailId;
	String otp;
	String newPassword;
	String confirmPassword;
	String oldPassword;
	String mobileNo;
}
