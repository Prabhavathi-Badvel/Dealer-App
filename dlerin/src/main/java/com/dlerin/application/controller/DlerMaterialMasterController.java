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
import com.dlerin.application.dto.ResponseDlerMaterialMasterDto;
import com.dlerin.application.dto.ResponseDlerMaterialMasterDto1;
import com.dlerin.application.entity.DlerMaterialMaster;
import com.dlerin.application.repository.DlerMaterialMasterRepo;
import com.dlerin.application.service.DlerMaterialMasterService;

@RestController
@PreAuthorize("hasAuthority('Dealer')")
public class DlerMaterialMasterController {

	@Autowired
	DlerMaterialMasterService dlerMaterialMasterService;

	@Autowired
	DlerMaterialMasterRepo dlerMaterialMasterRepo;

	ResponseDlerMaterialMasterDto response = new ResponseDlerMaterialMasterDto();
	ResponseDlerMaterialMasterDto1 response1 = new ResponseDlerMaterialMasterDto1();

	@PostMapping("/dlerin-add-dlermaterialmaster")
	public ResponseEntity<?> addDlerMetrialMaster(@RequestBody DlerMaterialMaster material) {

		try {

			DlerMaterialMaster addedMaterialMaster = dlerMaterialMasterService.add(material);

			if (addedMaterialMaster != null) {
				response.setMessage("Added Dler Material Master");
				response.setStatus(true);
				response.setAdded(addedMaterialMaster);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Failed to add/dler not present");
				response.setStatus(false);
				response.setAdded(null);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		} catch (Exception e) {
			response.setMessage("Record already exists");
			response.setStatus(false);
			response.setAdded(null);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

	}

	@PutMapping("/dlerin-update-dlermaterialmaster")
	public ResponseEntity<?> updateDlerMaterialMaster(@RequestBody DlerMaterialMaster material) {

		try {
			Optional<DlerMaterialMaster> exists = Optional
					.ofNullable(dlerMaterialMasterRepo.findByDlerIdMaterialId(material.getDlerIdMaterialId()));
			if (exists.isPresent()) {

				DlerMaterialMaster update = dlerMaterialMasterService.update(material);
				response.setMessage("Updated successfully");
				response.setStatus(true);
				response.setAdded(update);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Please check your dler id material id");
				response.setStatus(false);
				response.setAdded(null);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());

		}
	}

	@GetMapping("/dlerin-get-dlermaterialmaster")
	public ResponseEntity<?> getMaterialMaster(@RequestParam(required = false) String brandId,
			@RequestParam(required = false) String materialType, @RequestParam(required = false) String materialId,
			@RequestParam(required = false) String dlerId, @RequestParam(required = false) String materialName) {

		try {

			List<DlerMaterialMaster> dlerprofile = dlerMaterialMasterService.getDlerMaterialProfile(brandId,
					materialType, materialId, dlerId, materialName);
			if (dlerprofile != null && !dlerprofile.isEmpty()) {
				response1.setMessage("dler material master details");
				response1.setStatus(true);
				response1.setGetData(dlerprofile);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			} else {
				response1.setMessage("No details found for given parameters/check your parameters");
				response1.setStatus(false);
				response1.setGetData(dlerprofile);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}

	}
}
