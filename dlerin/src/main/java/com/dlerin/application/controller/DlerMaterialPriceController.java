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
import org.springframework.web.bind.annotation.RestController;
import com.dlerin.application.dto.DlerMaterialPriceDto;
import com.dlerin.application.dto.ResponseDlerMaterialPriceDto;
import com.dlerin.application.dto.ResponseDlerMaterialPriceDto1;
import com.dlerin.application.dto.ResponseDlerMaterialPriceDto2;
import com.dlerin.application.entity.DlerMaterialMaster;
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

	ResponseDlerMaterialPriceDto response = new ResponseDlerMaterialPriceDto();
	
	ResponseDlerMaterialPriceDto1 response1 = new ResponseDlerMaterialPriceDto1();

	ResponseDlerMaterialPriceDto2 response2 = new ResponseDlerMaterialPriceDto2();

	
	 @PostMapping("/dlerin-add-DlerMaterialPrices")
	    public ResponseEntity<?> addDlerMaterialPrices(@RequestBody List<DlerMaterialPrice> dmPrices) {
	       
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
	public ResponseEntity<?> getDlerPrice(@RequestBody DlerMaterialPriceDto dlerPrice) {

	 
	    DlerMaterialMaster searchCriteria = new DlerMaterialMaster();
	    searchCriteria.setDlerId(dlerPrice.getDlerId());
	    searchCriteria.setMaterialName(dlerPrice.getMaterialName());
	    searchCriteria.setSkuId(dlerPrice.getSkuId());
	    searchCriteria.setMaterialId(dlerPrice.getMaterialId());

	    List<DlerMaterialPrice> priceExists = dlerMaterialPriceService.getPrice(dlerPrice);
	    List<DlerMaterialMaster> allDlers = dlerMaterialPriceService.getDlersDetails(searchCriteria);

	    try {
	        if (priceExists!=null && !priceExists.isEmpty()) {
	            response1.setMessage("Get price details");
	            response1.setStatus(true);
	            response1.setPriceData(priceExists);
	            response1.setDlerDetails(allDlers);
	            return new ResponseEntity<>(response1, HttpStatus.OK);
	        } else {
	            response1.setMessage("No details found for the given parameters");
	            response1.setStatus(false);
	            response1.setPriceData(priceExists);
	            response1.setDlerDetails(allDlers);
	            return new ResponseEntity<>(response1, HttpStatus.OK);
	        }
	    } catch (Exception e) {
	    	return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
	    }
	}
}
