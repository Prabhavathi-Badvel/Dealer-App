package com.dlerin.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dlerin.application.dto.ResponseAdminBrandDto;
import com.dlerin.application.dto.ResponseAdminBrandMasterDto;
import com.dlerin.application.dto.ResponseAdminBrandMasterDto1;
import com.dlerin.application.entity.AdminBrandMaster;
import com.dlerin.application.repository.AdminBrandMasterRepo;
import com.dlerin.application.service.AdminBrandMasterService;

@RestController
public class AdminBrandMasterController {

	@Autowired
	AdminBrandMasterService adminBrandService;

	@Autowired
	AdminBrandMasterRepo admindBrandRepo;

	@PostMapping("/dlerin-add-adminbrands")
	@PreAuthorize("hasAuthority('Admin')")
	public ResponseEntity<?> addAdminBrands(@RequestBody AdminBrandMaster adminBrands) {
		ResponseAdminBrandMasterDto response = new ResponseAdminBrandMasterDto();

		try {
			AdminBrandMaster savedBrand = adminBrandService.addBrand(adminBrands);
			if (savedBrand != null) {
				response.setMessage("Brand added successfully");
				response.setStatus(true);
				response.setBrandsData(savedBrand);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Failed to add/admin not present");
				response.setStatus(true);
				response.setBrandsData(savedBrand);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setMessage("Record already exists");
			response.setStatus(false);
			response.setBrandsData(null);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PutMapping("/dlerin-update-adminbrands")
	@PreAuthorize("hasAuthority('Admin')")
	public ResponseEntity<?> updateAdminBrands(@RequestBody AdminBrandMaster brand) {
		boolean updated = adminBrandService.updateBrands(brand);
		ResponseAdminBrandMasterDto1 response1 = new ResponseAdminBrandMasterDto1();

		try {
			if (updated) {
				response1.setMessage("Updated successfully");
				response1.setStatus(true);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			} else {
				response1.setMessage("Failed to update brand");
				response1.setStatus(false);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
	}

	@GetMapping("/dlerin-get-adminbrands")
	@PreAuthorize("hasAuthority('Admin')")
	public ResponseEntity<?> getAdminBrandMaster(@RequestParam(required = false) String brandName,
			@RequestParam(required = false) String brandCategory,
			@RequestParam(required = false) String brandSubcategory) {
		ResponseAdminBrandDto response = new ResponseAdminBrandDto();

		try {
			List<AdminBrandMaster> brands = adminBrandService.getBrands(brandName, brandCategory, brandSubcategory);
			if (brands != null && !brands.isEmpty()) {
				response.setMessage("Successfully receieved brands");
				response.setStatus(true);
				response.setGetBrands(brands);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("No brands found with the provided parameters/check your parameters");
				response.setStatus(false);
				response.setGetBrands(brands);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
	}

	@GetMapping("/getCategoryOnly")
	public List<String> getDistinctBrandCategories() {
		return adminBrandService.getDistinctBrandCategories();
	}
}
