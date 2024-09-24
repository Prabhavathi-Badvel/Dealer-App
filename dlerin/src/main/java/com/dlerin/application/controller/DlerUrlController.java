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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.ResponseDlerUrlDto;
import com.dlerin.application.dto.ResponseUpdateDlerStoreMaterialDto;
import com.dlerin.application.dto.ResponseUpdateDlerUrlDto;
import com.dlerin.application.entity.DlerStoreMaterial;
import com.dlerin.application.entity.DlerUrl;
import com.dlerin.application.repository.DlerUrlRepo;
import com.dlerin.application.service.DlerUrlService;

@RestController
@PreAuthorize("hasAuthority('Dealer')")
@RequestMapping("/api")
public class DlerUrlController {
	
	
	@Autowired
	private DlerUrlService dlerUrlService;
	
	@Autowired
	private DlerUrlRepo dlerUrlRepo;
	
	@PostMapping("/dlerin-add-dlerUrl")
	public ResponseEntity<?> addDlerUrl(@RequestBody DlerUrl dlerUrl) {
		ResponseDlerUrlDto response = new ResponseDlerUrlDto();
		try {
			DlerUrl dealerUrl = dlerUrlService.addDlerUrl(dlerUrl);

			if (dealerUrl != null) {
				response.setMessage("Dealer Url details added successfully");
				response.setStatus(true);
				response.setData(dealerUrl);
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

    @PutMapping("/dlerin-update-dlerUrl")
	public ResponseEntity<?> updateDlerUrl(@RequestBody DlerUrl dlerUrl) {
    	ResponseDlerUrlDto response = new ResponseDlerUrlDto();
		try {
			Optional<Optional<DlerUrl>> exists = Optional
					.ofNullable(dlerUrlRepo.findByDlerUrl(dlerUrl.getUiUrl()));
			if (exists.isPresent()) {

				DlerUrl updateUiUrl = dlerUrlService.updateDlerUrl(dlerUrl);
				response.setMessage("Updated successfully");
				response.setStatus(true);
				response.setData(updateUiUrl);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Failed to update/Please check your  dleruiUrl");
				response.setStatus(false);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus(false);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}
	}
   
    @GetMapping("/dler-url")
    public ResponseEntity<?> getDlerUrl(
            @RequestParam(required = false) String uiUrl) {
        
    	ResponseUpdateDlerUrlDto response=new ResponseUpdateDlerUrlDto();
       
    	try {
    	List<DlerUrl> dlerUrl = dlerUrlService.getDlerUrl(uiUrl);
        if (dlerUrl != null || dlerUrl.isEmpty() ) {
        	response.setMessage("dler uiUrl  details");
        	response.setStatus(true);
        	response.setData(dlerUrl);
			return new ResponseEntity<>(response, HttpStatus.OK);
        }
         else {
        	 response.setMessage("No details found for given parameters/check your parameters");
        	 response.setStatus(false);
        	 response.setData(dlerUrl);
			 return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }catch(Exception e) {
		return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());

    	}
    }
}
