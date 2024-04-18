package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.AdminMetalMaster;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseAdminMaterialGetDto {
 
	private String message;
	private List<AdminMetalMaster> getData;
}
