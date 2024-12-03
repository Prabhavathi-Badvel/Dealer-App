package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.MembershipRenewalDetails;

import lombok.Data;

@Data
public class MembershipRenewalRequest {

	 private List<MembershipRenewalDetails> detailsList;
}
