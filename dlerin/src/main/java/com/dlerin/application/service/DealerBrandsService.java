package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.dto.ResponseCombinedDealerBrandsDto;
import com.dlerin.application.entity.DealerBrands;

public interface DealerBrandsService {

	public DealerBrands addBrands(DealerBrands brand);
	public DealerBrands updateBrands(DealerBrands brands);
	public List<DealerBrands> getBrands(String brandId,String updatedBy,String businessType);
	public ResponseCombinedDealerBrandsDto getBrandsAndAdmin(String brandId, String updatedBy, String businessType);
}
