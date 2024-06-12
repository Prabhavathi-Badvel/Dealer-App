package com.dlerin.application.dto;

import com.dlerin.application.entity.DlerInvoiceDetails;

import lombok.Data;

@Data
public class ResponseDlerInvioceDto {

	private String message;
	private boolean status;
	private DlerInvoiceDetails addData;
}
