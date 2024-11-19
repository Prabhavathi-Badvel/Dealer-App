package com.dlerin.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.PlanMembership;

@Repository
public interface PlanMembershipRepo extends JpaRepository<PlanMembership, String>{

	PlanMembership findByPlanId(String planId);

	List<PlanMembership> findByDefaultPlan(String defaultPlan);
}
