package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.DlerStoreDetails;

import lombok.Data;

@Data
public class ResponseUpdateDealerStoreDto {

	private String message;
	private boolean status;
	private List<DealerStoreDetailResponse> data;
}
