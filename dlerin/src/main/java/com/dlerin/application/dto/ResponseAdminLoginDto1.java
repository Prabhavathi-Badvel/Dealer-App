package com.dlerin.application.dto;

import com.dlerin.application.entity.AdminLogin;

import lombok.Data;

@Data
public class ResponseAdminLoginDto1 {

	private String message;
	private boolean status;
	private String jwtToken;
	private AdminLogin updatedData;

}
