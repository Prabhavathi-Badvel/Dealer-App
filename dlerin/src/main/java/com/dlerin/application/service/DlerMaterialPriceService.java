package com.dlerin.application.service;

import java.util.List;
import java.util.Map;

import com.dlerin.application.dto.DealerStoreMaterialResponse;
import com.dlerin.application.dto.DlerMaterialPriceDto;
import com.dlerin.application.entity.DlerMaterialMaster;
import com.dlerin.application.entity.DlerMaterialPrice;

public interface DlerMaterialPriceService {

//	public boolean updatePriceAndStoreHistory(DlerMaterialPrice price);

//	public List<DlerMaterialPrice> getPrice(DlerMaterialPriceDto dprice);

	public DealerStoreMaterialResponse getDealerPriceDetails(String skuId, String dlerId, String materialId);
	
	public List<DlerMaterialPrice> addPrices(List<DlerMaterialPrice> prices, String dlerId);
	
	public Map<String, String> updatePricesAndStoreHistory(List<DlerMaterialPrice> materials);
}
