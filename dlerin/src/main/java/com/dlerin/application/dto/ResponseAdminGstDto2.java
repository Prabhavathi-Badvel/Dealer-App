package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.AdminGst;

import lombok.Data;


@Data
public class ResponseAdminGstDto2 {
	
	private String message;
	private boolean status;
	private List<AdminGst> getData;
}
