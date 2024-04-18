package com.dlerin.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.DlerProfileDto;
import com.dlerin.application.dto.ResponseDlerProfileAddDto;
import com.dlerin.application.entity.AdminMetalMaster;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerProfile;
import com.dlerin.application.exception.DlerNotFoundException;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerProfileRepo;
import com.dlerin.application.serviceimpl.DlerProfileServiceImpl;

@RestController
public class DlerProfileController {

	@Autowired
	DlerProfileServiceImpl dlerProfileServiceImpl;

	@Autowired
	DlerProfileRepo dlerProfileRepo;

	@Autowired
	DlerBusinessLoginRepo dlerBusinessLoginRepo;

	@PostMapping("/dlerin-add-dlerprofile")
	public ResponseEntity<?> addDlerProfile(@RequestBody DlerProfile dp) {
		try {
			String dlerId = dp.getDlerId();

			Optional<DlerBusinessLogin> dlerOptional = dlerBusinessLoginRepo.findById(dlerId);

			if (dlerOptional.isPresent()) {

				if (!dlerProfileRepo.existsById(dp.getDlerBusinessId())) {

					DlerProfile addedDler = dlerProfileServiceImpl.addDler(dp, dlerId);

					if (addedDler != null) {
						ResponseDlerProfileAddDto rp = new ResponseDlerProfileAddDto();
						rp.setMessage("Dler Profile added successfully");
						rp.setDlerProfile(addedDler);
						return new ResponseEntity<>(rp, HttpStatus.OK);
					} else {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
								.body("Failed to add Dler Profile");
					}
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Record already exists");
				}
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Dler not found for given Dler ID: " + dp.getDlerId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred");
		}
	}

	@PutMapping("/dlerin-update-dlerprofile")
	public ResponseEntity<String> updateDlerProfle(@RequestBody DlerProfile dp) {

		String dlerBusinessId = dp.getDlerBusinessId();
		try {
			Optional<DlerProfile> businessIdExists = dlerProfileRepo.findById(dlerBusinessId);
			if (businessIdExists.isPresent()) {
				DlerProfile updated = dlerProfileServiceImpl.updateProfile(dp);
				return new ResponseEntity<>("Updated successfully", HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Dler profile doen't exist with : " + dp.getDlerBusinessId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("invalid business id", HttpStatus.OK);

	}

	@GetMapping("/dlerin-get-dlerprofile")
	public ResponseEntity<?> getDlerProfile(@RequestBody DlerProfileDto dpt) {

		String dlerBusinessName = dpt.getDlerBusinessName();
		String dlerBusinessLocation = dpt.getDlerBusinessLocation();
		String dlerBusinessContactNo = dpt.getDlerBusinessContactNo();
		String dlerBusinessContactPerson = dpt.getDlerBusinessContactPerson();
		try {
			List<DlerProfile> d = dlerProfileServiceImpl.getProfile(dlerBusinessName, dlerBusinessLocation,
					dlerBusinessContactPerson, dlerBusinessContactNo);

			return new ResponseEntity<>(d, HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}