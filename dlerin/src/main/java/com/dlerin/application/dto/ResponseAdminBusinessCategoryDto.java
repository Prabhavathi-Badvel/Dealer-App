package com.dlerin.application.dto;

import com.dlerin.application.entity.AdminBusinessCategory;
import lombok.Data;

@Data
public class ResponseAdminBusinessCategoryDto {

	private String message;
	private boolean status;
	private AdminBusinessCategory data;
}

