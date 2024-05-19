package com.dlerin.application.dto;

import com.dlerin.application.entity.DealerBrands;

import lombok.Data;


@Data
public class ResponseDealerBrnadsDto {

	private String message;
	private boolean status;
	private DealerBrands data;
}

