package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.DlerMaterialPrice;

import lombok.Data;


@Data
public class ResponseDlerMaterialPriceDto {

	private String message;
	private boolean status;
	private List<DlerMaterialPrice> addData;
}