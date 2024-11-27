package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.DlerMaterialImages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDlerMaterialImagesAddDto2 {

	private String message;
	private boolean status;
	private List<DlerMaterialImages> imageData;

	
}

