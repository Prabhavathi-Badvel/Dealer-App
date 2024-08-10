package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.dto.DealerMasterResponse;
import com.dlerin.application.dto.DlerResponse;
import com.dlerin.application.entity.DlerStoreDetails;

public interface DlerStoreDetailsService {
	public DlerStoreDetails addStore(DlerStoreDetails store);

	public DlerStoreDetails updateStore(DlerStoreDetails store);

	public List<DlerStoreDetails> getDlerStoreDetails(String location, String businessType, String storeId,
			String dlerId);

	public List<DlerStoreDetails> getAllDlerStoreDetails();

	public void deleteDlerStoreDetailsById(String dlerIdStoreId);

	public List<DlerResponse> getDataBy(String businessType, String location);

	public DealerMasterResponse getDealerDetails(String businessType, String location, String brandId,
			String businessName, String materialName);
}
