package com.dlerin.application.dto;

import com.dlerin.application.entity.AdminStoreVerification;

import lombok.Data;

@Data
public class ResponseAdminStoreVerificationDto {
	private String message;
	private boolean status;
	private AdminStoreVerification data;
}
