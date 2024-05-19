package com.dlerin.application.dto;

import com.dlerin.application.entity.DlerProfile;

import lombok.Data;


@Data
public class ResponseDlerProfileDto1 {
	private String message;
	private boolean status;
	private DlerProfile dlerProfile;
}
