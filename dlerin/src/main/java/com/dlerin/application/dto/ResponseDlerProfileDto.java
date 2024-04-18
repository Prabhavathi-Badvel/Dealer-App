package com.dlerin.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDlerProfileDto {
	private String message;
	private DlerProfileDto getDlerProfile;
}
