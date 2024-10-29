package com.dlerin.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.ResponseAdminBusinessCategoryDto;
import com.dlerin.application.dto.ResponseAdminUiEndPointDto;
import com.dlerin.application.dto.ResponseAdminUiEndPointDto2;
import com.dlerin.application.entity.AdminBusinessCategory;
import com.dlerin.application.entity.AdminUiEndPoint;
import com.dlerin.application.repository.AdminUiEndPointRepo;
import com.dlerin.application.service.AdminUiEndPointService;

@RestController
public class AdminUiEndPointController {
	
	@Autowired
	private AdminUiEndPointService adminUiEndPointService;
	
	@Autowired
	private AdminUiEndPointRepo adminUiEndPointRepo;
	
	@PostMapping("/dlerin-add-AdminUiEndPoint")
//	@PreAuthorize("hasAuthority('Admin')")
	public ResponseEntity<?> addAdminUiEndPoint(@RequestBody AdminUiEndPoint adminUiEndPoint) {
		String updatedBy = adminUiEndPoint.getUpdatedBy();
		ResponseAdminUiEndPointDto response = new ResponseAdminUiEndPointDto();
		try {

			AdminUiEndPoint adminUiEndPointUpdatedBy = adminUiEndPointService.addAdminUiEndPoint(adminUiEndPoint,
					updatedBy);

			if (adminUiEndPointUpdatedBy != null) {
				response.setMessage("added Admin Ui End Point ");
				response.setStatus(true);
				response.setData(adminUiEndPointUpdatedBy);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Failed to add/ dlerin add AdminUiEndPoint category not present or Record already exists");
				response.setStatus(false);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException e) {
			response.setMessage("Failed to add/dlerin add AdminUiEndPoint not present or Record already exists");
			response.setStatus(false);
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception e) {
			response.setMessage("An error occurred: " + e.getMessage());
			response.setStatus(false);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@GetMapping("/dlerin-get-AdminUiEndPoint")
	public ResponseEntity<?> getAdminUiEndPoint(@RequestParam(required = false) String systemId,
			@RequestParam(required = false) String ipUrlToUi,@RequestParam(required = false) String updatedBy) {
		ResponseAdminUiEndPointDto2 response = new ResponseAdminUiEndPointDto2();
		try {
			List<AdminUiEndPoint> getAdminUiEndPoint = adminUiEndPointService.getAdminUiEndPoint(systemId, ipUrlToUi,updatedBy);
			
			if (getAdminUiEndPoint != null && !getAdminUiEndPoint.isEmpty()) {
				response.setMessage("Receive Admin AdminUiEndPoint details");
				response.setStatus(true);
				response.setData(getAdminUiEndPoint);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("No Admin AdminUiEndPoint  details found for given parameters/check your parameter");
				response.setStatus(false);
				response.setData(getAdminUiEndPoint);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	@PutMapping("/dlerin-update-AdminUiEndPoint")
//	@PreAuthorize("hasAuthority('Admin')")
	public ResponseEntity<?> updateAdminBusinessCategory(@RequestBody AdminUiEndPoint adminUiEndPoint) {
		ResponseAdminUiEndPointDto response = new ResponseAdminUiEndPointDto();
		Optional<AdminUiEndPoint> gstExists = Optional.ofNullable(adminUiEndPointRepo.findBySystemId(adminUiEndPoint.getSystemId()));
		try {
			if (gstExists.isPresent()) {
				AdminUiEndPoint adminUiEndPointdetails = adminUiEndPointService.update(adminUiEndPoint);
				response.setMessage("updated successfully");
				response.setStatus(true);
				response.setData(adminUiEndPointdetails);
				return new ResponseEntity<>(response, HttpStatus.OK);

			} else {
				response.setMessage("Failed to update");
				response.setStatus(false);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
	}

}
