package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.MembershipRenewalDetails;

import lombok.Data;

@Data
public class ResponseAdminMembershipRenewalDetailsDto {
	private String message;
	private boolean status;
	private List<MembershipRenewalDetails> addedData;
}

