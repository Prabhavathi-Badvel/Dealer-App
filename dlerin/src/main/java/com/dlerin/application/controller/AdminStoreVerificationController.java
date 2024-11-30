package com.dlerin.application.controller;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.AdminStoreVerificationResponse;
import com.dlerin.application.dto.ResponseCombinedAdminStorAndStorMem;
import com.dlerin.application.dto.ResponseCombinedDealerBrandsDto;
import com.dlerin.application.dto.UpdateAdminStoreRequest;
import com.dlerin.application.entity.AdminStoreVerification;
import com.dlerin.application.repository.AdminStoreVerificationRepo;
import com.dlerin.application.service.AdminStoreVerificationService;

@RestController
public class AdminStoreVerificationController {

	@Autowired
	AdminStoreVerificationService adminStoreVerificationService;

	@Autowired
	AdminStoreVerificationRepo adminStoreVerificationRepo;

	@PostMapping("/dlerin-add-adminStoreVerification")
	public ResponseEntity<?> AddAdminStoreVerification(@RequestBody AdminStoreVerification adminStoreVerification) {
		return new ResponseEntity<>(adminStoreVerificationService.addAdminStore(adminStoreVerification), HttpStatus.OK);
	}

	@PutMapping("/dlerin-update-adminStoreVerification")
	public ResponseEntity<Map<String, Object>> updateAdminStoreVerifications(
			@RequestBody List<UpdateAdminStoreRequest> adminStoreRequests) {
		List<AdminStoreVerificationResponse> successResponses = new ArrayList<>();
		List<String> failureMessages = new ArrayList<>();

		for (UpdateAdminStoreRequest request : adminStoreRequests) {
			try {
				AdminStoreVerificationResponse response = adminStoreVerificationService
						.updateAdminStoreVerification(request);
				successResponses.add(response);
			} catch (RuntimeException ex) {

				failureMessages.add("Failed for storeId " + request.getStoreId() + ": " + ex.getMessage());
			}
		}

		Map<String, Object> result = new HashMap<>();
		result.put("success", successResponses);
		result.put("failures", failureMessages);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/dlerin-get-adminStoreVerification")
    public ResponseEntity<ResponseCombinedAdminStorAndStorMem> getadminStoreVerificationService(
            @RequestParam(required = false) String adminStoreVerificationId,
            @RequestParam(required = false) String storeId,
            @RequestParam(required = false) String dlerId,
            @RequestParam(required = false) String verificationStatus) {

		ResponseCombinedAdminStorAndStorMem response = adminStoreVerificationService.getadminStoreVerificationService(adminStoreVerificationId, storeId,
        		dlerId,verificationStatus);
        return ResponseEntity.ok(response);
    }
}
