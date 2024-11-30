package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.entity.DlerStoreMaterial;

public interface DlerStoreMaterialService {

	public DlerStoreMaterial addStoreMaterial(DlerStoreMaterial storeMaterial);

	public DlerStoreMaterial updateStore(DlerStoreMaterial storeMaterial);

	public List<DlerStoreMaterial> getDlerStoreMaterial(String storeIdSkuId,String skuId, String dlerId, String storeId);

	public List<DlerStoreMaterial> getAllDlerStoreMaterial();

	public void deleteDlerStoreMaterialById(String storeIdSkuId);

	public String getPrice(String dlerId, String skuId, String storeId);

}
