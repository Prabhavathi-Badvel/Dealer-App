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

import com.dlerin.application.dto.AdminMetalMasterDto;
import com.dlerin.application.dto.ResponseAdminmetalMaterialDto2;
import com.dlerin.application.dto.ResponseAdminMetalMasterDto;
import com.dlerin.application.dto.ResponseAdminMetalMasterDto1;
import com.dlerin.application.entity.AdminMetalMaster;
import com.dlerin.application.repository.AdminMetalMasterRepo;
import com.dlerin.application.service.AdminMetalMasterService;

@RestController
@PreAuthorize("hasAuthority('Admin')")
public class AdminMetalMasterController {

	@Autowired
	AdminMetalMasterService adminMetalMaterService;

	@Autowired
	AdminMetalMasterRepo adminMetalMasterRepo;

	ResponseAdminMetalMasterDto response = new ResponseAdminMetalMasterDto();

	ResponseAdminMetalMasterDto1 response1 = new ResponseAdminMetalMasterDto1();

	ResponseAdminmetalMaterialDto2 response2 = new ResponseAdminmetalMaterialDto2();

	@PostMapping("/dlerin-add-adminmetalmaster")
	public ResponseEntity<?> addAdminMaterial(@RequestBody AdminMetalMaster admin) {

	
		try {
			AdminMetalMaster add = adminMetalMaterService.addMaterial(admin);

				response.setMessage("Material added successfully");
				response.setStatus(true);
				response.setMaterialData(add);
				return new ResponseEntity<>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			response.setMessage("Record alredy exists");
			response.setStatus(false);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
	}

	@PutMapping("/dlerin-update-adminmetalmaster")
	public ResponseEntity<?> updateAdminMaterial(@RequestBody AdminMetalMaster adminMaterial) {

		Optional<AdminMetalMaster> materialIdExists = adminMetalMasterRepo.findById(adminMaterial.getMaterialId());
		try {
			if (materialIdExists.isPresent()) {
				AdminMetalMaster updatedMaterial = adminMetalMaterService.updateMaterial(adminMaterial);
				response1.setMessage("Updated successfully");
				response1.setStatus(true);
				response1.setMaterialData(updatedMaterial);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			} else {
				response1.setMessage("Failed to update/Please check your material id");
				response1.setStatus(false);
				return new ResponseEntity<>(response1, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@GetMapping("/dlerin-get-adminmetalmaster")
	public ResponseEntity<?> getAdminMaterial(@RequestBody AdminMetalMaster materials) {

		
		try {
				List<AdminMetalMaster> material = adminMetalMaterService.getMaterial(materials);
				response2.setMessage("Successfully received metals");
				response2.setStatus(true);
				response2.setGetData(material);
				return new ResponseEntity<>(response2, HttpStatus.OK);
			} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}
}