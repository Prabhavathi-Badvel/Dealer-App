package com.dlerin.application.dto;

import com.dlerin.application.entity.AdminGst;

import lombok.Data;

@Data
public class ResponseAdminGstDto1 {

	private String message;
	private boolean status;
	private AdminGst updatedData;
}
