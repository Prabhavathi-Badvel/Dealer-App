package com.dlerin.application.service;

import com.dlerin.application.dto.AdminStoreVerificationResponse;
import com.dlerin.application.dto.UpdateAdminStoreRequest;
import com.dlerin.application.entity.AdminStoreVerification;

public interface AdminStoreVerificationService {

	public AdminStoreVerification addAdminStore(AdminStoreVerification adminstore);

	public AdminStoreVerificationResponse updateAdminStoreVerification(UpdateAdminStoreRequest adminstore);
	
}
