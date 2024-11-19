package com.dlerin.application.serviceimpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.dto.PlanUpdateResponse;
import com.dlerin.application.entity.AdminLogin;
import com.dlerin.application.entity.PlanMembership;
import com.dlerin.application.entity.StoreMembership;
import com.dlerin.application.repository.AdminLoginRepo;
import com.dlerin.application.repository.PlanMembershipRepo;
import com.dlerin.application.repository.StoreMembershipRepo;
import com.dlerin.application.service.PlanMembershipService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PlanMembershipServiceImpl implements PlanMembershipService {

	@Autowired
	private PlanMembershipRepo planMembershipRepo;

	@Autowired
	private AdminLoginRepo adminLoginRepo;

	@Autowired
	private StoreMembershipRepo storeMembershipRepo;

	@Override
	public PlanMembership addPlanMembership(PlanMembership planMembership) {
		Optional<AdminLogin> login = Optional.ofNullable(adminLoginRepo.findByEmpId(planMembership.getUpdatedBy()));
		if (login.isPresent()) {
			AdminLogin admindb = login.get();
			PlanMembership existingPlanMembership = planMembershipRepo.findByPlanId(planMembership.getPlanId());

			if (existingPlanMembership == null) {
				planMembership.setUpdatedBy(admindb.getEmpId());
				planMembership.setDefaultPlan("x");
				return planMembershipRepo.save(planMembership);
			}
		}
		return null;
	}

	@Override
	public PlanUpdateResponse updatePlanAndStoreMembership(PlanMembership planMembership) {
		// Step 1: Retrieve the existing PlanMembership using the planId from the input
		// object
		Optional<PlanMembership> planOpt = planMembershipRepo.findById(planMembership.getPlanId());

		if (planOpt.isPresent()) {
			// Update the PlanMembership entity
			PlanMembership existingPlan = planOpt.get();
			existingPlan.setPlanName(planMembership.getPlanName());
			existingPlan.setAmount(planMembership.getAmount());
			existingPlan.setNumberOfDays(planMembership.getNumberOfDays());
			existingPlan.setUpdatedBy(planMembership.getUpdatedBy());
			planMembershipRepo.save(existingPlan);

			// Calculate the new expiry date
			LocalDate newExpiryDate = LocalDate.now().plusDays(planMembership.getNumberOfDays());

			// Step 2: Update related StoreMembership records
			List<StoreMembership> storeMemberships = storeMembershipRepo
					.findByStoreCurrentPlan(planMembership.getPlanId());

			// Check if any StoreMembership records were found
			if (storeMemberships.isEmpty()) {
				throw new EntityNotFoundException(
						"No StoreMembership records found for plan ID " + planMembership.getPlanId());
			}

			// Update each StoreMembership's fields and save
			storeMemberships.forEach(store -> {
				store.setStoreExpiryDate(newExpiryDate.toString());
				store.setStoreCurrentPlan(planMembership.getPlanId());
				store.setUpdatedBy(planMembership.getUpdatedBy());
				storeMembershipRepo.save(store);
			});

			return new PlanUpdateResponse(existingPlan, storeMemberships);
		} else {
			throw new EntityNotFoundException("PlanMembership with ID " + planMembership.getPlanId() + " not found");
		}
	}

}
