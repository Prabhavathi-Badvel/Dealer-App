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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dlerin.application.dto.ResponseAdminmetalMaterialDto2;
import com.dlerin.application.dto.ResponseAdminMetalMasterDto;
import com.dlerin.application.dto.ResponseAdminMetalMasterDto1;
import com.dlerin.application.entity.AdminMetalMaster;
import com.dlerin.application.repository.AdminMetalMasterRepo;
import com.dlerin.application.service.AdminMetalMasterService;

@RestController
public class AdminMetalMasterController {

	@Autowired
	AdminMetalMasterService adminMetalMaterService;

	@Autowired
	AdminMetalMasterRepo adminMetalMasterRepo;

	@PostMapping("/dlerin-add-adminmetalmaster")
//	@PreAuthorize("hasAuthority('Admin')")
	public ResponseEntity<?> addAdminMaterial(@RequestBody AdminMetalMaster admin) {
		ResponseAdminMetalMasterDto response = new ResponseAdminMetalMasterDto();
		try {
			AdminMetalMaster add = adminMetalMaterService.addMaterial(admin);

			response.setMessage("Material added successfully");
			response.setStatus(true);
			response.setMaterialData(add);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			response.setMessage("Record alredy exists");
			response.setStatus(false);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

	}

	@PutMapping("/dlerin-update-adminmetalmaster")
//	@PreAuthorize("hasAuthority('Admin')")
	public ResponseEntity<?> updateAdminMaterial(@RequestBody AdminMetalMaster adminMaterial) {
		ResponseAdminMetalMasterDto1 response1 = new ResponseAdminMetalMasterDto1();
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
				return new ResponseEntity<>(response1, HttpStatus.OK);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}

	}

	@GetMapping("/dlerin-get-adminmetalmaster")
//	@PreAuthorize("hasAuthority('Admin')")
	public ResponseEntity<?> getAdminMaterial(@RequestParam(required = false) String materialId,
			@RequestParam(required = false) String materialType, @RequestParam(required = false) String materialShape) {
		ResponseAdminmetalMaterialDto2 response2 = new ResponseAdminmetalMaterialDto2();
		try {
			List<AdminMetalMaster> material = adminMetalMaterService.getMaterial(materialId, materialType,
					materialShape);
			if (material != null && !material.isEmpty()) {
				response2.setMessage("Successfully received metals");
				response2.setStatus(true);
				response2.setGetData(material);
				return new ResponseEntity<>(response2, HttpStatus.OK);
			} else {
				response2.setMessage("No metals found with provided parameter/check your parameters");
				response2.setStatus(false);
				response2.setGetData(material);
				return new ResponseEntity<>(response2, HttpStatus.OK);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}

	}
	
	@GetMapping("/adminmetalmaster-getMaterialType")
	public List<String> getMaterialType() {
		return adminMetalMaterService.getDistinctMaterialType();
	}
	
	@GetMapping("/adminmetalmaster-getMaterialShape")
	public List<String> getMaterialShape() {
		return adminMetalMaterService.getDistinctMaterialShape();
	}
}