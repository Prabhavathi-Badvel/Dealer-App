package com.dlerin.application.dto;

import lombok.Data;

@Data
public class ResponseAdminMetalMasterDto {

	private String message;
	private boolean status;
	private AdminMetalMasterDto materialData;
	
}
