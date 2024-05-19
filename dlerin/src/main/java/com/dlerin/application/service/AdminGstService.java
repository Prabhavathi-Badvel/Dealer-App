package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.dto.AdminGstDto;
import com.dlerin.application.entity.AdminGst;

public interface AdminGstService {

	public AdminGst addGst(AdminGst adminGst,String email, String mobile);
	public AdminGst update(AdminGst admingst);
	public List<AdminGst> getDetails(AdminGst admingst);
}
