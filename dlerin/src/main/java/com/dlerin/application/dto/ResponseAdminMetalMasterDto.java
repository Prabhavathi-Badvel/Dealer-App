package com.dlerin.application.dto;

import com.dlerin.application.entity.AdminMetalMaster;

import lombok.Data;

@Data
public class ResponseAdminMetalMasterDto {

	private String message;
	private boolean status;
	private AdminMetalMaster materialData;
	
}
