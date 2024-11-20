package com.dlerin.application.dto;

import java.util.Map;

import lombok.Data;

@Data
public class ResponseDlerMaterialPriceDto2 {

	private String message;
	private boolean status;
	private Map<String, String> data;
	
}
