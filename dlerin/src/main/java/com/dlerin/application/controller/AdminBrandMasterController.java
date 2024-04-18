package com.dlerin.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.AdminBrandDto;
import com.dlerin.application.dto.ResponseAdminBrandGetDto;
import com.dlerin.application.dto.ResponseAdminBrandMasterDto;
import com.dlerin.application.entity.AdminBrandMaster;
import com.dlerin.application.repository.AdminBrandMasterRepo;
import com.dlerin.application.serviceimpl.AdminBrandMasterImpl;

@RestController
public class AdminBrandMasterController {

	@Autowired
	AdminBrandMasterImpl adminBrandServiceImpl;

	@Autowired
	AdminBrandMasterRepo admindBrandRepo;

	@PostMapping("/dlerin-add-adminbrands")
	public ResponseEntity<?> addAdminBrands(@RequestBody AdminBrandMaster adminBrands) {

		try {
			AdminBrandMaster savedBrand = adminBrandServiceImpl.addBrand(adminBrands);
			ResponseAdminBrandMasterDto responseAdminBrandto = new ResponseAdminBrandMasterDto();
			responseAdminBrandto.setMessage("Brand added successfully");
			responseAdminBrandto.setBrandsData(savedBrand);
			return new ResponseEntity<>(responseAdminBrandto, HttpStatus.OK);

		} catch (Exception e) {
			ResponseAdminBrandMasterDto errorResponse = new ResponseAdminBrandMasterDto();
			errorResponse.setMessage("Record already exists");
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/dlerin-update-adminbrands")
	public ResponseEntity<?> updateAdminBrands(@RequestBody AdminBrandMaster brand) {
		boolean updated = adminBrandServiceImpl.updateBrands(brand);
		try {
			if (updated) {
				return new ResponseEntity<>("Updated successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Failed to update brand", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping("/dlerin-get-adminbrands")
	public ResponseEntity<?> getAdminBrandMaster(@RequestBody AdminBrandDto adminBrandDto) {

		String brandName = adminBrandDto.getBrandName();
		String brandCategory = adminBrandDto.getBrandCategory();
		String brandSubcategory = adminBrandDto.getBrandSubcategory();
		try {
			ResponseAdminBrandGetDto responsebrand = new ResponseAdminBrandGetDto();
			responsebrand.setMassege("Successfully receieved brands from table");
			responsebrand.setGetBrands(adminBrandServiceImpl.getBrands(brandName, brandCategory, brandSubcategory));
			return new ResponseEntity<>(responsebrand, HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}
