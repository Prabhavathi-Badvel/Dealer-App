package com.dlerin.application.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.PlanUpdateResponse;
import com.dlerin.application.dto.ResponseAdminStoreVerificationDto;
import com.dlerin.application.dto.ResponsePlanMembershipDto;
import com.dlerin.application.entity.AdminStoreVerification;
import com.dlerin.application.entity.PlanMembership;
import com.dlerin.application.repository.PlanMembershipRepo;
import com.dlerin.application.service.PlanMembershipService;

@RestController
public class PlanMembershipServiceController {

	@Autowired
	private PlanMembershipService planMembershipService;
	
	@Autowired
	private PlanMembershipRepo planMembershipRepo;
	
	@PostMapping("/dlerin-add-planMembership")
	public ResponseEntity<?> AddPlanMembership(@RequestBody PlanMembership planMembership) {
		ResponsePlanMembershipDto response = new ResponsePlanMembershipDto();
		try {
			PlanMembership planMembershipDetails = planMembershipService.addPlanMembership(planMembership);

			if (planMembershipDetails != null) {
				response.setMessage("Plan Membership Details added successfully");
				response.setStatus(true);
				response.setData(planMembershipDetails);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("failed to add/admin not present or Record already exists");
				response.setStatus(false);
				response.setData(null);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setMessage("Record already exist");
			response.setStatus(false);
			response.setData(null);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
	
	@PutMapping("/dlerin-update-planmembership")
	public ResponseEntity<?> updatePlanAndStoreMembership(@RequestBody PlanMembership planMembership) {
		ResponsePlanMembershipDto response = new ResponsePlanMembershipDto();
		try {
			Optional<PlanMembership> exists =Optional.ofNullable(planMembershipRepo.findByPlanId(planMembership.getPlanId()));
			if (exists.isPresent()) {

				PlanUpdateResponse updateStore = planMembershipService.updatePlanAndStoreMembership(planMembership);
				response.setMessage("Updated successfully");
				response.setStatus(true);
				response.setData(updateStore.getUpdatedPlanMembership());
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Failed to update/Please check your  store_id and dler_id");
				response.setStatus(false);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus(false);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}
	}
}
