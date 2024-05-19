package com.dlerin.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.AdminBrandDto;
import com.dlerin.application.dto.ResponseAdminBrandDto;
import com.dlerin.application.dto.ResponseAdminBrandMasterDto;
import com.dlerin.application.entity.AdminBrandMaster;
import com.dlerin.application.repository.AdminBrandMasterRepo;
import com.dlerin.application.service.AdminBrandMasterService;

@RestController
@PreAuthorize("hasAuthority('Admin')")
public class AdminBrandMasterController {

	@Autowired
	AdminBrandMasterService adminBrandService;

	@Autowired
	AdminBrandMasterRepo admindBrandRepo;

	ResponseAdminBrandMasterDto response = new ResponseAdminBrandMasterDto();
	
	@PostMapping("/dlerin-add-adminbrands")
	public ResponseEntity<?> addAdminBrands(@RequestBody AdminBrandMaster adminBrands) {

		try {
			AdminBrandMaster savedBrand = adminBrandService.addBrand(adminBrands);
			
			response.setMessage("Brand added successfully");
			response.setStatus(true);
			response.setBrandsData(savedBrand);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			
			response.setMessage("Record already exists");
			response.setStatus(false);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/dlerin-update-adminbrands")
	public ResponseEntity<?> updateAdminBrands(@RequestBody AdminBrandMaster brand) {
		boolean updated = adminBrandService.updateBrands(brand);
		try {
			if (updated) {
				response.setMessage("Updated successfully");
				response.setStatus(true);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Failed to update brand");
				response.setStatus(false);
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping("/dlerin-get-adminbrands")
	public ResponseEntity<?> getAdminBrandMaster(@RequestBody AdminBrandDto adminBrandDto) {

		try {
			ResponseAdminBrandDto response = new ResponseAdminBrandDto();
			response.setMassege("Successfully receieved brands");
			response.setStatus(true);
			response.setGetBrands(adminBrandService.getBrands(adminBrandDto));
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}
