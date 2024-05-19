package com.dlerin.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.AdminGstDto;
import com.dlerin.application.dto.ResponseAdminGstDto;
import com.dlerin.application.dto.ResponseAdminGstDto1;
import com.dlerin.application.dto.ResponseAdminGstDto2;
import com.dlerin.application.entity.AdminGst;
import com.dlerin.application.repository.AdminGstRepo;
import com.dlerin.application.service.AdminGstService;

@RestController
@PreAuthorize("hasAuthority('Admin')")
public class AdminGstController {

	@Autowired
	AdminGstRepo adminGstRepo;

	@Autowired
	AdminGstService adminGstService;

	ResponseAdminGstDto response = new ResponseAdminGstDto();
	
	ResponseAdminGstDto1 response1 = new ResponseAdminGstDto1();
	
	@PostMapping("/dlerin-add-AdminGst")
	public ResponseEntity<?> AddAdminGst(@RequestBody AdminGst gst) {
     String email = gst.getEmailId();
     String mobile = gst.getMobileNo();
		
		try {

			AdminGst ad = adminGstService.addGst(gst,email,mobile);
			
			if (ad != null) {
				response.setMessage("added gst");
				response.setStatus(true);
				response.setAddedData(ad);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Record already exists");
				response.setStatus(false);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.getMessage();
			return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/dlerin-update-AdminGst")
	public ResponseEntity<?> updateAdminGst(@RequestBody AdminGst admin) {

		Optional<AdminGst> gstExists = Optional.ofNullable(adminGstRepo.findByGstCode(admin.getGstCode()));
		try {
			if (gstExists.isPresent()) {
				AdminGst gstAdmin = adminGstService.update(admin);
				response1.setMessage("updated successfully");
				response1.setStatus(true);
				response1.setUpdatedData(gstAdmin);
				return new ResponseEntity<>(response1, HttpStatus.OK);

			} else {
				response1.setMessage("Failed to update");
				response1.setStatus(false);
				return new ResponseEntity<>(response1, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.getMessage();
			return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/dlerin-get-AdminGst")
	public ResponseEntity<?> getDealerBrands(@RequestBody AdminGst adminGst) {

		List<AdminGst> getBrand = adminGstService.getDetails(adminGst);
		ResponseAdminGstDto2 response2 = new ResponseAdminGstDto2();
		response2.setMessage("dealer brands details");
		response2.setStatus(true);
		response2.setGetData(getBrand);

		return new ResponseEntity<>(response2, HttpStatus.OK);

	}

}
