package com.dlerin.application.service;


import com.dlerin.application.dto.PlanUpdateResponse;
import com.dlerin.application.entity.PlanMembership;

public interface PlanMembershipService {
	public PlanMembership addPlanMembership(PlanMembership planMembership);
	public PlanUpdateResponse updatePlanAndStoreMembership(PlanMembership planMembership);
}
