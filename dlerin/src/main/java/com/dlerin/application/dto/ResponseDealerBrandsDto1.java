package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.DealerBrands;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ResponseDealerBrandsDto1 {

	private String message;
	private boolean status;
	private List<DealerBrands> getData;
}

