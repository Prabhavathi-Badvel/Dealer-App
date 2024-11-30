package com.dlerin.application.service;


import java.util.List;

import com.dlerin.application.entity.PlanMembership;

public interface PlanMembershipService {
	public PlanMembership addPlanMembership(PlanMembership planMembership);
//	public PlanUpdateResponse updatePlanAndStoreMembership(PlanMembership planMembership);
	public PlanMembership updatePlanAndStoreMembership(PlanMembership planMembership);
	public List<PlanMembership> getPlanAndStoreMembership(String planId, String planName, String amount, String updatedBy, Long numberOfDays);
}
