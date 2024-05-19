package com.dlerin.application.dto;

import com.dlerin.application.entity.AdminBrandMaster;

import lombok.Data;


@Data
public class ResponseAdminBrandMasterDto {

	private String message;
	private boolean status;
	private AdminBrandMaster brandsData;
}
