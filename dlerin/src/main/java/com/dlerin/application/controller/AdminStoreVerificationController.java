package com.dlerin.application.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.ResponseAdminStoreVerificationDto;
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
	public ResponseEntity<?> updateAdminStoreVerification(@RequestBody UpdateAdminStoreRequest adminStoreVerification) {
		ResponseAdminStoreVerificationDto response = new ResponseAdminStoreVerificationDto();
		try {
			Optional<AdminStoreVerification> exists =adminStoreVerificationRepo.findByAdminStatusUpdatedBy(adminStoreVerification.getAdminStatusUpdatedBy());
			if (exists.isPresent()) {

				AdminStoreVerification updateStore = adminStoreVerificationService.updateAdminStoreVerification(adminStoreVerification);
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
}
