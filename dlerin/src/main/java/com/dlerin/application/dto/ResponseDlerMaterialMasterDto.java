package com.dlerin.application.dto;

import com.dlerin.application.entity.DlerMaterialMaster;

import lombok.Data;


@Data
public class ResponseDlerMaterialMasterDto {

	private String message;
	private boolean status;
	private DlerMaterialMaster added;
}
