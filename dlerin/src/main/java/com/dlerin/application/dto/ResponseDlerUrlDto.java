package com.dlerin.application.dto;

import com.dlerin.application.entity.DlerUrl;

import lombok.Data;

@Data
public class ResponseDlerUrlDto {

	private String message;
	private boolean status;
	private DlerUrl data;
}

