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

import com.dlerin.application.dto.ProfileDto;
import com.dlerin.application.dto.ResponseDlerProfileDto;
import com.dlerin.application.dto.ResponseDlerProfileDto1;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerProfile;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerProfileRepo;
import com.dlerin.application.service.DlerProfileService;

@RestController
@PreAuthorize("hasAuthority('Dealer')")
public class DlerProfileController {

	@Autowired
	DlerProfileService dlerProfileService;

	@Autowired
	DlerProfileRepo dlerProfileRepo;

	@Autowired
	DlerBusinessLoginRepo dlerBusinessLoginRepo;

	ResponseDlerProfileDto1 response1 = new ResponseDlerProfileDto1();

	@PostMapping("/dlerin-add-dlerprofile")
	public ResponseEntity<?> addDlerProfile(@RequestBody DlerProfile dp) {

		try {

			Optional<DlerBusinessLogin> dlerOptional = dlerBusinessLoginRepo.findById(dp.getDlerId());

			if (dlerOptional.isPresent()) {

				if (!dlerProfileRepo.existsById(dp.getDlerBusinessId())) {

					DlerProfile addedDler = dlerProfileService.addDler(dp);

					if (addedDler != null) {
						response1.setMessage("Dler Profile added successfully");
						response1.setStatus(true);
						response1.setDlerProfile(addedDler);
						return new ResponseEntity<>(response1, HttpStatus.OK);
					} else {
						response1.setMessage("Failed to add Dler Profile");
						response1.setStatus(false);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response1);
					}
				} else {
					response1.setMessage("Record already exists");
					response1.setStatus(false);
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response1);
				}
			} else {
				response1.setMessage("Dler not found for given Dler ID: " + dp.getDlerId());
				response1.setStatus(false);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response1);
			}
		} catch (Exception e) {
			response1.setMessage(e.getMessage());
			response1.setStatus(false);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response1);
		}
	}

	@PutMapping("/dlerin-update-dlerprofile")
	public ResponseEntity<?> updateDlerProfle(@RequestBody DlerProfile dp) {

		String dlerBusinessId = dp.getDlerBusinessId();
		try {

			Optional<DlerProfile> businessIdExists = dlerProfileRepo.findById(dlerBusinessId);
			if (businessIdExists.isPresent()) {
				DlerProfile updated = dlerProfileService.updateProfile(dp);
				response1.setMessage("Updated successfully");
				response1.setStatus(true);
				response1.setDlerProfile(updated);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			} else {
				response1.setMessage("Dler profile doen't exist with : " + dp.getDlerBusinessId());
				response1.setStatus(false);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response1.setMessage("invalid business id");
		response1.setStatus(false);
		return new ResponseEntity<>(response1, HttpStatus.OK);

	}

	@GetMapping("/dlerin-get-dlerprofile")
	public ResponseEntity<?> getDlerProfile(@RequestBody ProfileDto dealerProfile
			) {

		try {

			List<DlerProfile> dler = dlerProfileService.getProfile(dealerProfile);
			ResponseDlerProfileDto response = new ResponseDlerProfileDto();
			response.setMessage("Dler details");
			response.setStatus(true);
			response.setGetDlerProfile(dler);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}