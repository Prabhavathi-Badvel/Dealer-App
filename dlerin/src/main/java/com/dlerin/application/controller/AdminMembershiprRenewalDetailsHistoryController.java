package com.dlerin.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.ResponseAdminMembershipRenewalDetailsDto;
import com.dlerin.application.entity.MembershipRenewalDetails;
import com.dlerin.application.service.MembershipRenewalService;

@RestController
public class AdminMembershiprRenewalDetailsHistoryController {

	@Autowired
	private MembershipRenewalService membershipRenewalService;

	@GetMapping("/dlerin-get-membershipRenewalDetailsHistory")
	public ResponseEntity<?> getMembershipRenewalDetails(@RequestParam(required = false) String membershipOrderIdLineItem,
			@RequestParam(required = false) String orderPlacedBy, @RequestParam(required = false) String fromDate,
			@RequestParam(required = false) String toDate,@RequestParam(required = false)String storeIdKey) {
		ResponseAdminMembershipRenewalDetailsDto response = new ResponseAdminMembershipRenewalDetailsDto();
		try {
			List<MembershipRenewalDetails> getMembershipRenewalDetails = membershipRenewalService.getMembershipRenewalDetails(membershipOrderIdLineItem, orderPlacedBy,fromDate,toDate,storeIdKey);
			
			if (getMembershipRenewalDetails != null && !getMembershipRenewalDetails.isEmpty()) {
				response.setMessage("Receive Membership Renewal details History");
				response.setStatus(true);
				response.setAddedData(getMembershipRenewalDetails);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("No Membership Renewal details found for given parameters/check your parameter");
				response.setStatus(false);
				response.setAddedData(getMembershipRenewalDetails);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
	}
}
