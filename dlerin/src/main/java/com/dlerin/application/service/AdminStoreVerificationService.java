package com.dlerin.application.service;

import com.dlerin.application.dto.AdminStoreVerificationResponse;
import com.dlerin.application.dto.ResponseCombinedAdminStorAndStorMem;
import com.dlerin.application.dto.UpdateAdminStoreRequest;
import com.dlerin.application.entity.AdminStoreVerification;

public interface AdminStoreVerificationService {

	public AdminStoreVerification addAdminStore(AdminStoreVerification adminstore);

	public AdminStoreVerificationResponse updateAdminStoreVerification(UpdateAdminStoreRequest adminstore);

	public ResponseCombinedAdminStorAndStorMem getadminStoreVerificationService(String adminStoreVerificationId,
			String storeId, String dlerId, String verificationStatus);
	
}
