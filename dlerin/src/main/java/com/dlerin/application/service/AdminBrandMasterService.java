package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.entity.AdminBrandMaster;

public interface AdminBrandMasterService {

	public AdminBrandMaster addBrand(AdminBrandMaster adminBrand);

	public boolean updateBrands(AdminBrandMaster adminBrand);

	public List<AdminBrandMaster> getBrands(String brandCatSubCat,String brandName, String brandCategory, String brandSubcategory);

	public List<String> getDistinctBrandCategories();

	public List<String> getBrandSubcategory();

	public List<String> getDistinctBrandIds();
}
