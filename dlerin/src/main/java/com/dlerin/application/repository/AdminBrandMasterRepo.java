package com.dlerin.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.AdminBrandMaster;

@Repository
public interface AdminBrandMasterRepo extends JpaRepository<AdminBrandMaster, String>{


	List<AdminBrandMaster> findByBrandName(String brandName);

	Optional<AdminBrandMaster> findByBrandIdOrBrandName(String brandId, String brandName);

	AdminBrandMaster findByBrandCatSubCat(String brandCatSubCat);

	List<AdminBrandMaster> findByBrandNameAndBrandCategoryAndBrandSubcategory(String brandName, String brandCategory, String brandSubcategory);

	List<AdminBrandMaster> findByBrandNameAndBrandCategory(String brandName, String brandCategory);

	List<AdminBrandMaster> findByBrandNameAndBrandSubcategory(String brandName, String brandSubcategory);

	List<AdminBrandMaster> findByBrandCategoryAndBrandSubcategory(String brandCategory, String brandSubcategory);

	AdminBrandMaster findByBrandId(String brandId);

}
