package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.entity.DealerBrands;

public interface DealerBrandsService {

	public DealerBrands addBrands(DealerBrands brand);
	public DealerBrands updateBrands(DealerBrands brands);
	public List<DealerBrands> getBrands(DealerBrands brands);
}
