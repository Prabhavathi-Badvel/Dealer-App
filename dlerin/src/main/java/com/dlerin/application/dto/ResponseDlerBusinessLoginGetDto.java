package com.dlerin.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDlerBusinessLoginGetDto {

	private String message;
	private DlerBusinessLoginDto getDlerProfile;
}
