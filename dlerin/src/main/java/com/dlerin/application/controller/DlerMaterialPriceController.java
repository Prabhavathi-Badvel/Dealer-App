package com.dlerin.application.controller;

import java.util.List;

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

import com.dlerin.application.dto.DealerStoreMaterialResponse;
import com.dlerin.application.dto.ResponseDlerMaterialPriceDto;
import com.dlerin.application.dto.ResponseDlerMaterialPriceDto2;
import com.dlerin.application.entity.DlerMaterialPrice;
import com.dlerin.application.repository.DlerMaterialPriceRepo;
import com.dlerin.application.service.DlerMaterialPriceService;

@RestController
@PreAuthorize("hasAuthority('Dealer')")
public class DlerMaterialPriceController {

	@Autowired
	DlerMaterialPriceService dlerMaterialPriceService;

	@Autowired
	DlerMaterialPriceRepo dlerMaterialPriceRepo;

	@PostMapping("/dlerin-add-DlerMaterialPrices")
	public ResponseEntity<?> addDlerMaterialPrices(@RequestBody List<DlerMaterialPrice> dmPrices) {
		ResponseDlerMaterialPriceDto response = new ResponseDlerMaterialPriceDto();
		try {
			List<DlerMaterialPrice> addedPrices = dlerMaterialPriceService.addPrices(dmPrices);

			if (!addedPrices.isEmpty()) {
				response.setMessage("Added successfully");
				response.setStatus(true);
				response.setAddData(addedPrices);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Record already exists or given DlerIdMaterialId not found");
				response.setStatus(false);
				response.setAddData(null);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setMessage("Failed to add prices");
			response.setStatus(false);
			response.setAddData(null);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PutMapping("/dlerin-update-DlerMaterialPrice")
	public ResponseEntity<?> updateDlerMaterialPrice(@RequestBody DlerMaterialPrice price) {
		boolean updateSuccess = dlerMaterialPriceService.updatePriceAndStoreHistory(price);
		ResponseDlerMaterialPriceDto2 response2 = new ResponseDlerMaterialPriceDto2();
		try {

			if (updateSuccess) {
				response2.setMessage("Updated successfully");
				response2.setStatus(true);
				return new ResponseEntity<>(response2, HttpStatus.OK);
			} else {
				response2.setMessage("Failed to update/MaterialIDPriceId not found");
				response2.setStatus(false);
				return new ResponseEntity<>(response2, HttpStatus.OK);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());

		}

	}

	@GetMapping("/dlerin-get-DlerMaterialPrice")
	public ResponseEntity<DealerStoreMaterialResponse> getDealerPriceDetails(
			@RequestParam(required = false) String skuId, @RequestParam(required = false) String dlerId,
			@RequestParam(required = false) String materialId) {
		DealerStoreMaterialResponse response = dlerMaterialPriceService.getDealerPriceDetails(skuId, dlerId,
				materialId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
