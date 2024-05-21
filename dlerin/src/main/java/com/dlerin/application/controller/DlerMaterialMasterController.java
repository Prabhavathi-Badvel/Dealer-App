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
	
	@PostMapping("/dlerin-add-dlermaterialmaster")
	public ResponseEntity<?> AddDlerMetrialMaster(@RequestBody DlerMaterialMaster material) {
		
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
		            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		        }

		    } catch (Exception e) {
		        e.getMessage();
		        return new ResponseEntity<>("Record already exists", HttpStatus.BAD_REQUEST);
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
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.getMessage();
			return new ResponseEntity<>("invalid id", HttpStatus.BAD_REQUEST);
			
		}
	}

	@GetMapping("/dlerin-get-dlermaterialmaster")
	public ResponseEntity<?> getDlerProfile(@RequestBody DlerMaterialMaster material) {

		try {

			List<DlerMaterialMaster> dlerprofile = dlerMaterialMasterService.getProfileDlerMaterial(material);
			ResponseDlerMaterialMasterDto1 response = new ResponseDlerMaterialMasterDto1();
			response.setMessage("dler material master details");
			response.setStatus(true);
			response.setGetData(dlerprofile);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.getMessage();
		}
		return null;

	}
}
