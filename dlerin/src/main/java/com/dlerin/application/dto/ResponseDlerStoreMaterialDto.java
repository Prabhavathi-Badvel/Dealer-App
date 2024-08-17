package com.dlerin.application.dto;

import com.dlerin.application.entity.DlerStoreMaterial;

import lombok.Data;

@Data
public class ResponseDlerStoreMaterialDto {

	private String message;
	private boolean status;
	private DlerStoreMaterial data;
}

