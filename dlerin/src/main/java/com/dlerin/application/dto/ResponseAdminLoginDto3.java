package com.dlerin.application.dto;

import lombok.Data;

@Data
public class ResponseAdminLoginDto3 {

	private String message;
	private boolean status;
	private AdminLoginDto adminData;
}
