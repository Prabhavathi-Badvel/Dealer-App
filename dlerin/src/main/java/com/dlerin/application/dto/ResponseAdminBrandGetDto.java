package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.AdminBrandMaster;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseAdminBrandGetDto {

	private String massege;
	private List<AdminBrandMaster> getBrands;
}
