package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.dto.AdminBrandDto;
import com.dlerin.application.entity.AdminBrandMaster;

public interface AdminBrandMasterService {

	public AdminBrandMaster addBrand(AdminBrandMaster adminBrand);

	public List<AdminBrandMaster> getBrands(String brandName, String brandCategory, String brandSubcategory);
}
