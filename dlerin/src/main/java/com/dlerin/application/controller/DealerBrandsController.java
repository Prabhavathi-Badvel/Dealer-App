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

import com.dlerin.application.dto.ResponseDealerBrandsDto1;
import com.dlerin.application.dto.ResponseDealerBrnadsDto;
import com.dlerin.application.entity.DealerBrands;
import com.dlerin.application.repository.DealerBrandsRepo;
import com.dlerin.application.service.DealerBrandsService;

@RestController
@PreAuthorize("hasAuthority('Dealer')")
public class DealerBrandsController {

	@Autowired
	DealerBrandsService dealerBrandsService;

	@Autowired
	DealerBrandsRepo dealerBrandsRepo;

	ResponseDealerBrnadsDto response = new ResponseDealerBrnadsDto();

	@PostMapping("/dlerin-add-dealerBrands")
	public ResponseEntity<?> AddDealerBrands(@RequestBody DealerBrands brands) {

		try {
			DealerBrands dealerBrands = dealerBrandsService.addBrands(brands);

			if (dealerBrands != null) {
				response.setMessage("Dealer brands details added successfully");
				response.setStatus(true);
				response.setData(dealerBrands);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Dealer not present");
				response.setStatus(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setMessage("Record already exist");
			response.setStatus(false);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/dlerin-update-dealerBrands")
	public ResponseEntity<?> updateDealerBrands(@RequestBody DealerBrands dealerBrands) {

		try {
			Optional<DealerBrands> exists = Optional
					.ofNullable(dealerBrandsRepo.findByBrandIdDlerId(dealerBrands.getBrandIdDlerId()));
			if (exists.isPresent()) {

				DealerBrands updateDealer = dealerBrandsService.updateBrands(dealerBrands);
				response.setMessage("Updated successfully");
				response.setStatus(true);
				response.setData(updateDealer);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Please check your  brand_id_dler_id");
				response.setStatus(false);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus(false);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}
	}

	@GetMapping("/dlerin-get-dealerBtrands")
	public ResponseEntity<?> getDealerBrands(@RequestBody DealerBrands dBrands) {
		try {
			List<DealerBrands> getBrand = dealerBrandsService.getBrands(dBrands);
			ResponseDealerBrandsDto1 response1 = new ResponseDealerBrandsDto1();
			response1.setMessage("dealer brands details");
			response1.setStatus(true);
			response1.setGetData(getBrand);

			return new ResponseEntity<>(response1, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}