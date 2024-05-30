package com.dlerin.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.dlerin.application.dto.ResponseDlerOrderDetails;
import com.dlerin.application.dto.ResponseDlerOrderDetails1;
import com.dlerin.application.entity.DlerOrderDetails;
import com.dlerin.application.service.DlerOrderDetailsService;

@RestController
@PreAuthorize("hasAuthority('Dealer')")
public class DlerOrderDetailsController {

	@Autowired
	DlerOrderDetailsService orderService;

	ResponseDlerOrderDetails response = new ResponseDlerOrderDetails();

	ResponseDlerOrderDetails1 response1 = new ResponseDlerOrderDetails1();

	@PostMapping("/dlerin-add-orders")
	public ResponseEntity<?> addOrderDetails(@RequestBody List<DlerOrderDetails> order) {

		try {
			List<DlerOrderDetails> orderDetailsList = orderService.addOrders(order);
			if (!orderDetailsList.isEmpty()) {
				response.setMessage("Added successfully");
				response.setStatus(true);
				response.setOrderData(orderDetailsList);
				return new ResponseEntity<>(response, HttpStatus.OK);

			} else {
				response.setMessage("failed to add/dlerId not present");
				response.setStatus(false);
				response.setOrderData(null);
				return new ResponseEntity<>(response, HttpStatus.OK);

			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus(false);
			response.setOrderData(null);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PutMapping("/dlerin-update-dlerorderdetails")
	public ResponseEntity<?> updateOrderDetails(@RequestBody DlerOrderDetails orders) {
		try {
			DlerOrderDetails updateOrders = orderService.updateOrder(orders);
			if (updateOrders != null) {
				response1.setMessage("updated successfully");
				response1.setStatus(true);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			} else {
				response1.setMessage("failed to update/orderid not found");
				response1.setStatus(false);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}

	}
}
