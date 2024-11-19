package com.dlerin.application.dto;

import com.dlerin.application.entity.VerificationStatus;

import lombok.Data;

@Data
public class UpdateAdminStoreRequest {
	
	private String adminStoreVerificationId;

	private String storeId;

	private String dlerId;

	private VerificationStatus verificationStatus;

	private String verifcationComment;

	private String updatedBy;

	private String planId;
	
	private String defaultPlan;
}
