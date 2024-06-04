package com.dlerin.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.ResponseDlerMaterialAvailabilityDto;
import com.dlerin.application.entity.DlerMaterialAvailability;
import com.dlerin.application.service.DlerMaterialAvailabilityService;

@RestController
@PreAuthorize("hasAuthority('Dealer')")
public class DlerMaterialAvailabilityController {
	
	@Autowired
	DlerMaterialAvailabilityService availabilityService;
	
	ResponseDlerMaterialAvailabilityDto response = new ResponseDlerMaterialAvailabilityDto();
	
	@GetMapping("/get-material-availability")
	public ResponseEntity<?> getAvailability(@RequestParam(required = false)String dlerId,@RequestParam(required = false)String dlerIdMaterialId){
		try {
			List<DlerMaterialAvailability> availability = availabilityService.getMaterialAvailability(dlerId, dlerIdMaterialId);
			if(availability!= null && !availability.isEmpty()) {
				response.setMessage("dler material availability details");
				response.setStatus(true);
				response.setGetData(availability);
				return new ResponseEntity<>(response,HttpStatus.OK);
			}else {
				response.setMessage("failed to get/check your params/param doestnot exists");
				response.setStatus(false);
				response.setGetData(availability);
				return new ResponseEntity<>(response,HttpStatus.OK);
			}
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
		
		
	}

}
