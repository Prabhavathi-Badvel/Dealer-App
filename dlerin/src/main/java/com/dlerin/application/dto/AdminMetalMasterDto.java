package com.dlerin.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminMetalMasterDto {

	private String materialId;
	private String materialType;
	private String materialWidth;
	private String materialLength;
	private String materialThickness;
	private String materialShape;
	private String widthInUnits;
	private String lengthInUnits;
	private String thicknessUnits;
}
