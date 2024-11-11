package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.PlanMembership;
import com.dlerin.application.entity.StoreMembership;

import lombok.Data;

@Data
public class PlanUpdateResponse {

	public PlanUpdateResponse(PlanMembership existingPlan, List<StoreMembership> updatedStores) {
		// TODO Auto-generated constructor stub
	}

	private PlanMembership updatedPlanMembership;
	private List<StoreMembership> updatedStoresMembership;
}
