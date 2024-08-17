package com.dlerin.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.DealerMasterResponse;
import com.dlerin.application.dto.DlerResponse;
import com.dlerin.application.dto.ResponseDealerStoreDto;
import com.dlerin.application.dto.ResponseUpdateDealerStoreDto;
import com.dlerin.application.entity.DlerStoreDetails;
import com.dlerin.application.repository.DlerStoreDetailsRepo;
import com.dlerin.application.service.DlerStoreDetailsService;

@RestController
@PreAuthorize("hasAuthority('Dealer')")
@RequestMapping("/api")
public class DlerStoreDetailsController {

	@Autowired
	private DlerStoreDetailsService dlerStoreDetailsService;

	@Autowired
	private DlerStoreDetailsRepo dlerStoreDetailsRepo;

	@PostMapping("/dlerin-add-dealerStore")
	public ResponseEntity<?> AddDealerStore(@RequestBody DlerStoreDetails store) {
		ResponseDealerStoreDto response = new ResponseDealerStoreDto();
		try {
			DlerStoreDetails dealerStores = dlerStoreDetailsService.addStore(store);

			if (dealerStores != null) {
				response.setMessage("Dealer brands details added successfully");
				response.setStatus(true);
				response.setData(dealerStores);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Dealer not present");
				response.setStatus(false);
				response.setData(null);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setMessage("Record already exist");
			response.setStatus(false);
			response.setData(null);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PutMapping("/dlerin-update-dealerStore")
	public ResponseEntity<?> updateDealerStore(@RequestBody DlerStoreDetails dealerStore) {
		ResponseDealerStoreDto response = new ResponseDealerStoreDto();
		try {
			Optional<DlerStoreDetails> exists = Optional
					.ofNullable(dlerStoreDetailsRepo.findByDlerIdStoreId(dealerStore.getDlerIdStoreId()));
			if (exists.isPresent()) {

				DlerStoreDetails updateStore = dlerStoreDetailsService.updateStore(dealerStore);
				response.setMessage("Updated successfully");
				response.setStatus(true);
				response.setData(updateStore);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Failed to update/Please check your  store_id_dler_id");
				response.setStatus(false);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus(false);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}
	}

	@GetMapping("/dler-store-details")
	public ResponseEntity<?> getDlerStoreDetails(@RequestParam(required = false) String location,
			@RequestParam(required = false) String businessType, @RequestParam(required = false) String storeId,
			@RequestParam(required = false) String dlerId) {

		ResponseUpdateDealerStoreDto response = new ResponseUpdateDealerStoreDto();
		try {
			List<DlerStoreDetails> dlerProfile = dlerStoreDetailsService.getDlerStoreDetails(location, businessType,
					storeId, dlerId);

			if (dlerProfile != null || dlerProfile.isEmpty()) {
				response.setMessage("dler store details");
				response.setStatus(true);
				response.setData(dlerProfile);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("No details found for given parameters/check your parameters");
				response.setStatus(false);
				response.setData(dlerProfile);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
	}

	@DeleteMapping("/dler-store-details/{dlerIdStoreId}")
	public ResponseEntity<String> deleteDlerStoreDetailsById(@PathVariable String dlerIdStoreId) {
		dlerStoreDetailsService.deleteDlerStoreDetailsById(dlerIdStoreId);
		return ResponseEntity.ok("Data deleted successfully");
	}

	@GetMapping("/search-product")
	public ResponseEntity<List<DlerResponse>> getDataBy(@RequestParam String businessType,
			@RequestParam(required = false) String location) {

		List<DlerResponse> responses = dlerStoreDetailsService.getDataBy(businessType, location);

		if (responses.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(responses);
	}

	@GetMapping("/home-search")
	public ResponseEntity<DealerMasterResponse> getDealerDetails(@RequestParam(required = false) String businessType,
			@RequestParam(required = false) String location, @RequestParam(required = false) String brandId,
			@RequestParam(required = false) String businessName, @RequestParam(required = false) String materialName) {
		DealerMasterResponse response = dlerStoreDetailsService.getDealerDetails(businessType, location, brandId,
				businessName, materialName);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}