package com.dlerin.application.dto;

import com.dlerin.application.entity.VerificationStatus;

import lombok.Data;

@Data
public class StoreMembershipResponse{
	private String storeExpiryDate;
	private String storeCurrentPlan;
	private VerificationStatus verificationStatus;
	private String verificationComment;
}
