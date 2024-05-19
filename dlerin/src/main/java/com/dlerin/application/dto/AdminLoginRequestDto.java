package com.dlerin.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginRequestDto {

	String emailId;
	String mobileNo;
	String password;
}
