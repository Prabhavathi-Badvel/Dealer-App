package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.MembershipRenewalDetails;
import com.dlerin.application.entity.MembershipRenewalHeader;

import lombok.Data;

@Data
public class ResponseAdminMembershipRenewalHeaderDto {
	private String message;
	private boolean status;
	private List<MembershipRenewalHeader> addedData;
}

