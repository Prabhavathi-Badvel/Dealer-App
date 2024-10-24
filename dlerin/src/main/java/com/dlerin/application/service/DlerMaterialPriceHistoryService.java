package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.entity.DlerMaterialPriceHistory;

public interface DlerMaterialPriceHistoryService {

	public List<DlerMaterialPriceHistory> getPriceHistory(String price, String dlerIdMaterialId, String id,
			String dlerId);
}
