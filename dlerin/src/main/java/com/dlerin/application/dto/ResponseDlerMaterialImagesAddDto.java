package com.dlerin.application.dto;

import com.dlerin.application.entity.DlerMaterialImages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDlerMaterialImagesAddDto {

	private String message;
	private DlerMaterialImages imageData;

	
}

