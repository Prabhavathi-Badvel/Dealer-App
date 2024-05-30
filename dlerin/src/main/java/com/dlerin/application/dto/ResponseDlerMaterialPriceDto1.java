package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.DlerMaterialMaster;
import com.dlerin.application.entity.DlerMaterialPrice;

import lombok.Data;


@Data
public class ResponseDlerMaterialPriceDto1 {

	private String message;
	private boolean status;
	private List<DlerMaterialMaster> dlerDetails;
	private List<DlerMaterialPrice> priceData;
}