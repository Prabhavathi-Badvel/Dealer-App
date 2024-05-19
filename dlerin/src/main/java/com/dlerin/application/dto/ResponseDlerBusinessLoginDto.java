package com.dlerin.application.dto;

import lombok.Data;

@Data
public class ResponseDlerBusinessLoginDto {

	private String message;
	private boolean status;
	private DlerBusinessLoginDto DlerProfile;
	
}
