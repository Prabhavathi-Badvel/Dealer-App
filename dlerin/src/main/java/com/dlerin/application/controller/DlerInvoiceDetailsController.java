package com.dlerin.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.dlerin.application.dto.ResponseDlerInvioceDto;
import com.dlerin.application.entity.DlerInvoiceDetails;
import com.dlerin.application.service.DlerInvoiceDetailsService;

@RestController
@PreAuthorize("hasAuthority('Dealer')")
public class DlerInvoiceDetailsController {

	@Autowired
	DlerInvoiceDetailsService InvoiceService;
	
	@PostMapping("/invoice details")
	public ResponseEntity<?> SaveInvioce(@RequestBody DlerInvoiceDetails invoice){
		ResponseDlerInvioceDto response = new ResponseDlerInvioceDto();
		try {
			DlerInvoiceDetails details = InvoiceService.saveInvoceDetails(invoice);
			
			if(details != null) {
				response.setMessage("added invoice details");
				response.setStatus(true);
				response.setAddData(details);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}else {
				response.setMessage("failed to add/orderid not present");
				response.setStatus(false);
				response.setAddData(details);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			
		}catch(Exception e){
			response.setMessage(e.getMessage());
			response.setStatus(false);
			response.setAddData(null);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		
	}
}
