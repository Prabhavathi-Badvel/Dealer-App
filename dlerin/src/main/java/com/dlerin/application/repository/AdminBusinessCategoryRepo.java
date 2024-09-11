package com.dlerin.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.AdminBusinessCategory;

@Repository
public interface AdminBusinessCategoryRepo extends JpaRepository<AdminBusinessCategory, String> {

	AdminBusinessCategory findByBusinessCategoryId(String businessCategoryId);
	
	List<AdminBusinessCategory> findByBusinessCategoryIdAndBusinessCategoryName(String businessCategoryId, String businessCategoryName);
}
