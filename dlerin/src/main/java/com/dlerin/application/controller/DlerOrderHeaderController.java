package com.dlerin.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.DlerOrderDetailsDto;
import com.dlerin.application.dto.ResponseDlerOrderHeaderDto;
import com.dlerin.application.dto.ResponseListOrderDto;
import com.dlerin.application.entity.DlerOrderDetails;
import com.dlerin.application.entity.DlerOrderHeader;
import com.dlerin.application.service.DlerOrderHeaderService;

@RestController
@PreAuthorize("hasAuthority('Dealer')")
public class DlerOrderHeaderController {

	@Autowired
	DlerOrderHeaderService dlerOrderHeaderService;

	ResponseDlerOrderHeaderDto response = new ResponseDlerOrderHeaderDto();
	ResponseListOrderDto response1 = new ResponseListOrderDto();

	@PutMapping("/update-dlerorderheader")
	public ResponseEntity<?> updateHeader(@RequestBody DlerOrderHeader header) {

		try {

			DlerOrderHeader update = dlerOrderHeaderService.updateHeaderDetails(header);
			if (update != null) {
				response.setMessage("update successsfully");
				response.setStatus(true);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage(" failed to update/orderid not present");
				response.setStatus(true);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		} catch (Exception e) {
			e.getMessage();
		}
		return null;

	}

	@PreAuthorize("hasAuthority('Admin')")
	@GetMapping("/orderdetails")
	public ResponseEntity<?> getOrders(@RequestBody DlerOrderDetailsDto order) {

		String orderId = order.getOrderId();
		String orderBy = order.getOrderBy();
		String fromDate = order.getFromDate();
		String toDate = order.getToDate();

		try {
			List<DlerOrderDetails> entity = dlerOrderHeaderService.getOrderData(orderId, orderBy, fromDate, toDate);
			if (!entity.isEmpty()) {
				response1.setMessage("Order details fetched successfully.!");
				response1.setStatus(true);
				response1.setData(entity);
				return ResponseEntity.ok(response1);
			} else {
				response1.setMessage("No data found for the given details.!");
				response1.setStatus(true);
				return ResponseEntity.status(HttpStatus.OK).body(response1);
			}
		} catch (Exception e) {
			response1.setMessage(e.getMessage());
			response1.setStatus(false);
			return ResponseEntity.status(HttpStatus.OK).body(response1);
		}

	}

}
