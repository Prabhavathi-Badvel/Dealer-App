package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.AdminBrandMaster;

import lombok.Data;


@Data
public class ResponseAdminBrandDto {

	private String massege;
	private boolean status;
	private List<AdminBrandMaster> getBrands;
}
