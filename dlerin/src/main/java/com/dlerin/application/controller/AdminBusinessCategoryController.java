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

import com.dlerin.application.dto.AdminBusinessCategoryDTO;
import com.dlerin.application.dto.ResponseAdminBusinessCategoryDto;
import com.dlerin.application.dto.ResponseAdminBusinessCategoryDto2;
import com.dlerin.application.entity.AdminBusinessCategory;
import com.dlerin.application.repository.AdminBusinessCategoryRepo;
import com.dlerin.application.service.AdminBusinessCategoryService;

@RestController
public class AdminBusinessCategoryController {

	@Autowired
	private AdminBusinessCategoryService adminBusinessCategoryService;
	
	@Autowired
	private AdminBusinessCategoryRepo adminBusinessCategoryRepo;

	@PostMapping("/dlerin-add-AdminBusinessCategory")
	@PreAuthorize("hasAuthority('Admin')")
	public ResponseEntity<?> addAdminBusinessCategory(@RequestBody AdminBusinessCategory  adminBusinessCategory) {
		String empId = adminBusinessCategory.getEmpId();
		ResponseAdminBusinessCategoryDto response = new ResponseAdminBusinessCategoryDto();
		try {

			AdminBusinessCategory abc = adminBusinessCategoryService.addAdminBusinessCategory(adminBusinessCategory,empId);

			if (abc != null) {
				response.setMessage("added admin buinsess category");
				response.setStatus(true);
				response.setData(abc);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}  else {
	            response.setMessage("Failed to add/admin business category not present or Record already exists");
	            response.setStatus(false);
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        }
	    } catch (DataIntegrityViolationException e) {
	        response.setMessage("Failed to add/admin business category not present or Record already exists");
	        response.setStatus(false);
	        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	    } catch (Exception e) {
	        response.setMessage("An error occurred: " + e.getMessage());
	        response.setStatus(false);
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	}
	
	@GetMapping("/dlerin-get-AdminBusinessCategory")
	public ResponseEntity<?> getAdminBusinessCategory(@RequestParam(required = false) String businessCategoryId,
			@RequestParam(required = false) String businessCategoryName) {
		ResponseAdminBusinessCategoryDto2 response = new ResponseAdminBusinessCategoryDto2();
		try {
			List<AdminBusinessCategory> getBrand = adminBusinessCategoryService.getAdminBusinessCategory(businessCategoryId, businessCategoryName);
			
			if (getBrand != null && !getBrand.isEmpty()) {
				response.setMessage("Received gst details");
				response.setStatus(true);
				response.setAddedData(getBrand);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("No admin gst  details found for given parameters/check your parameter");
				response.setStatus(false);
				response.setAddedData(getBrand);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	@PutMapping("/dlerin-update-AdminBusinessCategory")
	@PreAuthorize("hasAuthority('Admin')")
	public ResponseEntity<?> updateAdminBusinessCategory(@RequestBody AdminBusinessCategory abc) {
		ResponseAdminBusinessCategoryDto response = new ResponseAdminBusinessCategoryDto();
		Optional<AdminBusinessCategory> gstExists = Optional.ofNullable(adminBusinessCategoryRepo.findByBusinessCategoryId(abc.getBusinessCategoryId()));
		try {
			if (gstExists.isPresent()) {
				AdminBusinessCategory adminBusinessCategory = adminBusinessCategoryService.update(abc);
				response.setMessage("updated successfully");
				response.setStatus(true);
				response.setData(adminBusinessCategory);
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
