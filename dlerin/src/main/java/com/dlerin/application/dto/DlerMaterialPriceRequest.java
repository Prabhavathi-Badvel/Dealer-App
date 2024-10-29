package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.DlerMaterialPrice;

import lombok.Data;

@Data
public class DlerMaterialPriceRequest {
	private String dlerId;
	private List<DlerMaterialPrice> materials;
}
