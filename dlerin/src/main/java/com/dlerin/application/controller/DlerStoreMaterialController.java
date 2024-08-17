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

import com.dlerin.application.dto.ResponseDlerStoreMaterialDto;
import com.dlerin.application.dto.ResponseUpdateDlerStoreMaterialDto;
import com.dlerin.application.entity.DlerStoreMaterial;
import com.dlerin.application.repository.DlerStoreMaterialRepo;
import com.dlerin.application.service.DlerStoreMaterialService;

@RestController
@PreAuthorize("hasAuthority('Dealer')")
@RequestMapping("/api")
public class DlerStoreMaterialController {

	@Autowired
	private DlerStoreMaterialService dlerStoreMaterialService;
	
	@Autowired 
	private DlerStoreMaterialRepo dlerStoreMaterialRepo;
	
	@PostMapping("/dlerin-add-storeMaterial")
	public ResponseEntity<?> addStoreMaterial(@RequestBody DlerStoreMaterial storeMaterial) {
		ResponseDlerStoreMaterialDto response = new ResponseDlerStoreMaterialDto();
		try {
			DlerStoreMaterial dealerStoresMaterial = dlerStoreMaterialService.addStoreMaterial(storeMaterial);

			if (dealerStoresMaterial != null) {
				response.setMessage("Dealer store Material details added successfully");
				response.setStatus(true);
				response.setData(dealerStoresMaterial);
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
	
//	@PreAuthorize("hasAuthority('Dealer')")
	@GetMapping("/getPrice")
	    public String getPrice(@RequestParam String dlerId, 
	                           @RequestParam String skuId, 
	                           @RequestParam String storeId) {
	        return dlerStoreMaterialService.getPrice(dlerId, skuId, storeId);
	}

    @PutMapping("/dlerin-update-storeMaterial")
	public ResponseEntity<?> updateStoreMaterial(@RequestBody DlerStoreMaterial storeMaterial) {
    	ResponseDlerStoreMaterialDto response = new ResponseDlerStoreMaterialDto();
		try {
			Optional<DlerStoreMaterial> exists = Optional
					.ofNullable(dlerStoreMaterialRepo.findByStoreIdSkuId(storeMaterial.getStoreIdSkuId()));
			if (exists.isPresent()) {

				DlerStoreMaterial updateStore = dlerStoreMaterialService.updateStore(storeMaterial);
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
    
    @GetMapping("/dler-store-material")
    public ResponseEntity<?> getDlerStoreMaterial(
            @RequestParam(required = false) String skuId,
            @RequestParam(required = false) String dlerId ,@RequestParam(required = false) String  storeId) {
        
    	ResponseUpdateDlerStoreMaterialDto response=new ResponseUpdateDlerStoreMaterialDto();
       
    	try {
    	List<DlerStoreMaterial> dlerstorematerial = dlerStoreMaterialService.getDlerStoreMaterial(skuId, dlerId,storeId);
        if (dlerstorematerial != null || dlerstorematerial.isEmpty() ) {
        	response.setMessage("dler store material  details");
        	response.setStatus(true);
        	response.setData(dlerstorematerial);
			return new ResponseEntity<>(response, HttpStatus.OK);
        }
         else {
        	 response.setMessage("No details found for given parameters/check your parameters");
        	 response.setStatus(false);
        	 response.setData(dlerstorematerial);
			 return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }catch(Exception e) {
		return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());

    	}
    }
    
    @DeleteMapping("/dler-store-material/{storeIdSkuId}")
    public ResponseEntity<String> deleteDlerStoreDetailsById(@PathVariable String storeIdSkuId) {
    	dlerStoreMaterialService.deleteDlerStoreMaterialById(storeIdSkuId);
        return ResponseEntity.ok("Data deleted successfully");
    }
   
}
