package com.dlerin.application.dto;

import lombok.Data;


@Data
public class ResponseDlerBusinessLoginDto1 {

	private String message;
	private boolean status;
	private DlerBusinessLoginDto2 getDlerProfile;
}
