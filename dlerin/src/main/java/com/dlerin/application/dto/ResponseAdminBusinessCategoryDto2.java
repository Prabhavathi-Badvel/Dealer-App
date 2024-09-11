package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.AdminBusinessCategory;

import lombok.Data;

@Data
public class ResponseAdminBusinessCategoryDto2 {
	private String message;
	private boolean status;
	private List<AdminBusinessCategory> addedData;
}

