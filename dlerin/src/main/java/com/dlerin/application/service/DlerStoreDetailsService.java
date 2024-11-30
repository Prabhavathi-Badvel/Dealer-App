package com.dlerin.application.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.dlerin.application.dto.DealerMasterResponse;
import com.dlerin.application.dto.DealerStoreDetailResponse;
import com.dlerin.application.dto.DlerResponse;
import com.dlerin.application.entity.DlerStoreDetails;

public interface DlerStoreDetailsService {
	public DlerStoreDetails addStore(DlerStoreDetails store);

	public DlerStoreDetails updateStore(DlerStoreDetails store);

	public List<DealerStoreDetailResponse> getDlerStoreDetails(String location, String businessType, String storeId,
			String dlerId);

	public List<DlerStoreDetails> getAllDlerStoreDetails();

	public void deleteDlerStoreDetailsById(String dlerIdStoreId);

	public List<DlerResponse> getDataBy(String businessType, String location);

	public DealerMasterResponse getDealerDetails(String businessType, String location, String brandId,
			String businessName, String materialName);

	public List<String> getAllStoreLocation();
	
	public ResponseEntity<?> uploadStoreGstDocs(String dlerIdStoreId, MultipartFile gstDocument, MultipartFile tradeLicense);
	
	public List<String> getDistinctLocationsByBusinessType(String businessType);
}
