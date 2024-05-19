package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.DlerMaterialImages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDlerMaterialImagesGetDto {

	private String message;
	private List<DlerMaterialImages> getData;
}
