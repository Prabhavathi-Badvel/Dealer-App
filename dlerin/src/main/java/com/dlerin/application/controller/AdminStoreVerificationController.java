package com.dlerin.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.AdminStoreVerificationResponse;
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
	public ResponseEntity<AdminStoreVerificationResponse> updateAdminStoreVerification(
	        @RequestBody UpdateAdminStoreRequest adminstore) {
	    AdminStoreVerificationResponse response = adminStoreVerificationService.updateAdminStoreVerification(adminstore);
	    return ResponseEntity.ok(response);
	}

}
