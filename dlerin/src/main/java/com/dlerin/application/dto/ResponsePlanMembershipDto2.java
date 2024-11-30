package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.PlanMembership;

import lombok.Data;

@Data
public class ResponsePlanMembershipDto2 {
	private String message;
	private boolean status;
	private List<PlanMembership> data;
}
