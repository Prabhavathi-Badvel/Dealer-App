package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.DlerOrderDetails;

import lombok.Data;

@Data
public class ResponseDlerOrderDetails {

	private String message;
	private boolean status;
	private List<DlerOrderDetails> orderData;
}
