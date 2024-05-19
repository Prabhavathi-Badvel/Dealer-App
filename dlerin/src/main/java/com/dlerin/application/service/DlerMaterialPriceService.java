package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.dto.DlerMaterialPriceDto;
import com.dlerin.application.entity.DlerMaterialPrice;

public interface DlerMaterialPriceService {

	public List<DlerMaterialPrice> addPrices(List<DlerMaterialPrice> prices);
	public boolean updatePriceAndStoreHistory(DlerMaterialPrice price);
	public List<DlerMaterialPrice> get(DlerMaterialPriceDto dprice);
}
