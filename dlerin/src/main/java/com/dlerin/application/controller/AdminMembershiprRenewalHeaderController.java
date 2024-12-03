package com.dlerin.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.CustomResponseMembershipRenewal;
import com.dlerin.application.dto.ResponseAdminMembershipRenewalHeaderDto;
import com.dlerin.application.entity.MembershipRenewalHeader;
import com.dlerin.application.service.MembershipRenewalService;

@RestController
public class AdminMembershiprRenewalHeaderController {

	@Autowired
	private MembershipRenewalService membershipRenewalService;

	@PutMapping("/dlerin-update-membershipRenewalHeader")
	public ResponseEntity<CustomResponseMembershipRenewal> updateStatus(
			@RequestBody List<MembershipRenewalHeader> request) {
		CustomResponseMembershipRenewal response = membershipRenewalService
				.updateMembershipRenewalHeaderStatus(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/dlerin-get-membershipRenewalHeader")
	public ResponseEntity<?> getMembershipRenewalHeader(@RequestParam(required = false) String membershipOrderId,
			@RequestParam(required = false) String orderPlacedBy, @RequestParam(required = false) String status,
			@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate) {
		ResponseAdminMembershipRenewalHeaderDto response = new ResponseAdminMembershipRenewalHeaderDto();
		try {
			List<MembershipRenewalHeader> getMembershipRenewalHeader = membershipRenewalService
					.getMembershipRenewalHeader(membershipOrderId, orderPlacedBy, status, fromDate, toDate);

			if (getMembershipRenewalHeader != null && !getMembershipRenewalHeader.isEmpty()) {
				response.setMessage("Receive Membership Renewal Header");
				response.setStatus(true);
				response.setAddedData(getMembershipRenewalHeader);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("No Membership Renewal header found for given parameters/check your parameter");
				response.setStatus(false);
				response.setAddedData(getMembershipRenewalHeader);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
	}

}
