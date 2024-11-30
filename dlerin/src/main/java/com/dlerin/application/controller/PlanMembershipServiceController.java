package com.dlerin.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.ResponsePlanMembershipDto;
import com.dlerin.application.dto.ResponsePlanMembershipDto2;
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

				PlanMembership updateStore = planMembershipService.updatePlanAndStoreMembership(planMembership);
				response.setMessage("Updated successfully");
				response.setStatus(true);
				response.setData(updateStore);
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
	
	@GetMapping("/dlerin-get-planmembership")
	public ResponseEntity<?> getPlanAndStoreMembership(@RequestParam(required = false) String planId,
			@RequestParam(required = false) String planName, @RequestParam(required = false) String amount,
			@RequestParam(required = false) String updatedBy,@RequestParam(required = false) Long numberOfDays) {
		ResponsePlanMembershipDto2 response1 = new ResponsePlanMembershipDto2();

		try {
			List<PlanMembership> getBrand = planMembershipService.getPlanAndStoreMembership(planId, planName, amount,updatedBy,numberOfDays);
			if (getBrand != null && !getBrand.isEmpty()) {

				response1.setMessage("plan Membership Service details");
				response1.setStatus(true);
				response1.setData(getBrand);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			} else {
				response1.setMessage("No details found for provided parameters/check your parameters");
				response1.setStatus(false);
				response1.setData(getBrand);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
	}
}
