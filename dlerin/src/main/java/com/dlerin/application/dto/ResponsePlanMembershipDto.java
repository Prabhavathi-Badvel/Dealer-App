package com.dlerin.application.dto;

import com.dlerin.application.entity.PlanMembership;

import lombok.Data;

@Data
public class ResponsePlanMembershipDto {
	private String message;
	private boolean status;
	private PlanMembership data;
}
