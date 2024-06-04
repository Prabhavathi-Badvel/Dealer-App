package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.DlerMaterialPriceHistory;

import lombok.Data;

@Data
public class ResponseDlerMaterialPriceHistoryDto {

	private String message;
	private boolean status;
	private List<DlerMaterialPriceHistory> getData;
}
