package com.dlerin.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.ResponseDlerMaterialPriceHistoryDto;
import com.dlerin.application.entity.DlerMaterialPriceHistory;
import com.dlerin.application.service.DlerMaterialPriceHistoryService;

@RestController
@PreAuthorize("hasAuthority('Dealer')")
public class DlerMaterialPriceHistoryController {

	@Autowired
	DlerMaterialPriceHistoryService priceHistoryService;

	@GetMapping("/get-price-history")
	public ResponseEntity<?> getDlerPriceHistory(@RequestParam(required = false) String materialIdPriceId,
			@RequestParam(required = false) String dlerIdMaterialId, @RequestParam(required = false) String id,
			@RequestParam(required = false) String dlerId) {
		ResponseDlerMaterialPriceHistoryDto response = new ResponseDlerMaterialPriceHistoryDto();
		try {
			List<DlerMaterialPriceHistory> history = priceHistoryService.getPriceHistory(materialIdPriceId,
					dlerIdMaterialId, id, dlerId);
			if (history != null && !history.isEmpty()) {
				response.setMessage("price history");
				response.setStatus(true);
				response.setGetData(history);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("failed to get/check your params");
				response.setStatus(true);
				response.setGetData(history);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.getMessage();
		}

		return null;

	}

}
