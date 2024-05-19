package com.dlerin.application.dto;

import lombok.Data;

@Data
public class ResponseAdminLoginDto2 {
	private String message;
	private boolean status;
	private String jwtToken;
	private AdminLoginDto adminData;
}
