package com.dlerin.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DlerMaterialPriceDto {

	private String materialIdPriceId;
	private String dlerIdMaterialId;
	private String price;
	private String priceUpdatedDate;
	private String priceUpdatedBy;
	private String currency = "INR";
	private String ordQty;
	private String discount;
	private String gstCode;
	private String stockAvailable;
	private String materialId;
	private String materialName;
	private String skuId;

}

