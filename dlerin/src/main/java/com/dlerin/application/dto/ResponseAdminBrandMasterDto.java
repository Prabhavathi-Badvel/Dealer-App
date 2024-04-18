package com.dlerin.application.dto;

import com.dlerin.application.entity.AdminBrandMaster;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseAdminBrandMasterDto {

	private String message;
	private AdminBrandMaster brandsData;
}
