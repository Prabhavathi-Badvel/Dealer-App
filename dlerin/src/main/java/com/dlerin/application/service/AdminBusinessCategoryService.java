package com.dlerin.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dlerin.application.entity.AdminBusinessCategory;

@Service
public interface AdminBusinessCategoryService {

	public AdminBusinessCategory addAdminBusinessCategory(AdminBusinessCategory adminBusinessCategory, String name);
	
	public List<AdminBusinessCategory> getAdminBusinessCategory(String businessCategoryId, String businessCategoryName);
	
	public AdminBusinessCategory update(AdminBusinessCategory adminBusinessCategory);
}
