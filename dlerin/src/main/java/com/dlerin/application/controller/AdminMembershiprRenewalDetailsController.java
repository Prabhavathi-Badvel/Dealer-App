package com.dlerin.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.CustomResponseMembershipRenewal;
import com.dlerin.application.dto.MembershipRenewalRequest;
import com.dlerin.application.dto.ResponseAdminBusinessCategoryDto2;
import com.dlerin.application.dto.ResponseAdminMembershipRenewalDetailsDto;
import com.dlerin.application.dto.ResponseCombinedAdminStorAndStorMem;
import com.dlerin.application.dto.ResponseDlerOrderDetails;
import com.dlerin.application.dto.ResponseHeaderDto;
import com.dlerin.application.dto.ResponseListOrderDto;
import com.dlerin.application.dto.ResponseMembershipRenewalDetailsDto;
import com.dlerin.application.entity.AdminBusinessCategory;
import com.dlerin.application.entity.DlerOrderDetails;
import com.dlerin.application.entity.DlerOrderHeader;
import com.dlerin.application.entity.MembershipRenewalDetails;
import com.dlerin.application.entity.MembershipRenewalHeader;
import com.dlerin.application.service.MembershipRenewalDetailsService;
import com.dlerin.application.service.MembershipRenewalService;

import jakarta.mail.MessagingException;

@RestController
public class AdminMembershiprRenewalDetailsController {

	@Autowired
	private MembershipRenewalService membershipRenewalService;

	@PostMapping("/dlerin-add-membershipRenewalDetails")
	public ResponseEntity<MembershipRenewalHeader> processMembershipRenewal(
			@RequestBody MembershipRenewalRequest request) {
		List<MembershipRenewalDetails> detailsList = request.getDetailsList();

		MembershipRenewalHeader header = membershipRenewalService.processMembershipRenewal(detailsList);

		return new ResponseEntity<>(header, HttpStatus.CREATED);
	}

	@PutMapping("/dlerin-update-membershipRenewalDetails")
	public ResponseEntity<CustomResponseMembershipRenewal> updateStatus(
			@RequestBody List<MembershipRenewalDetails> request) {
		CustomResponseMembershipRenewal response = membershipRenewalService
				.updateMembershipRenewalDetailsStatus(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/dlerin-get-membershipRenewalDetails")
	public ResponseEntity<?> getMembershipRenewalDetails(@RequestParam(required = false) String membershipOrderIdLineItem,
			@RequestParam(required = false) String orderPlacedBy, @RequestParam(required = false) String fromDate,
			@RequestParam(required = false) String toDate,@RequestParam(required = false)String storeIdKey) {
		ResponseAdminMembershipRenewalDetailsDto response = new ResponseAdminMembershipRenewalDetailsDto();
		try {
			List<MembershipRenewalDetails> getMembershipRenewalDetails = membershipRenewalService.getMembershipRenewalDetails(membershipOrderIdLineItem, orderPlacedBy,fromDate,toDate,storeIdKey);
			
			if (getMembershipRenewalDetails != null && !getMembershipRenewalDetails.isEmpty()) {
				response.setMessage("Receive Membership Renewal details");
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
