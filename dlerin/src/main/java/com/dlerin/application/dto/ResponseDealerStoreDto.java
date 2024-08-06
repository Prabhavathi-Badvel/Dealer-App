package com.dlerin.application.dto;

import com.dlerin.application.entity.DlerStoreDetails;
import lombok.Data;

@Data
public class ResponseDealerStoreDto {

	private String message;
	private boolean status;
	private DlerStoreDetails data;
}

