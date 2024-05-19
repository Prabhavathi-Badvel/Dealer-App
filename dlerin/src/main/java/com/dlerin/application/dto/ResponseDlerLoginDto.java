package com.dlerin.application.dto;

import lombok.Data;

@Data
public class ResponseDlerLoginDto {
	private String message;
	private boolean status;
	private String jwtToken;
	private DlerBusinessLoginDto1 loginDetails;
	
	
}