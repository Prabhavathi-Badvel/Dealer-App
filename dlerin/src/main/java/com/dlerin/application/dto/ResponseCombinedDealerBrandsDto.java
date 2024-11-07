package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.AdminBrandMaster;
import com.dlerin.application.entity.DealerBrands;

import lombok.Data;

@Data
public class ResponseCombinedDealerBrandsDto {

	private String message;
    private boolean status;
    private List<DealerBrands> dealerBrands;
    private List<AdminBrandMaster> adminBrandMaster;
    private String error;
}
