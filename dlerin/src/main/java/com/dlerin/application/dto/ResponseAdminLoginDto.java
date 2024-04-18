package com.dlerin.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ResponseAdminLoginDto {

	private String message;
	private AdminLoginDto adminData;

}
