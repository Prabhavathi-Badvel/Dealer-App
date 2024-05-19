package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.AdminMetalMaster;

import lombok.Data;


@Data
public class ResponseAdminmetalMaterialDto2 {
 
	private String message;
	private boolean status;
	private List<AdminMetalMaster> getData;
}
