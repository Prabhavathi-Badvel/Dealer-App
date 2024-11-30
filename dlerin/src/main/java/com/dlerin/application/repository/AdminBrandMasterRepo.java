package com.dlerin.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.AdminBrandMaster;

@Repository
public interface AdminBrandMasterRepo extends JpaRepository<AdminBrandMaster, String> {

	AdminBrandMaster findByBrandCatSubCat(String brandCatSubCat);

	@Query("SELECT DISTINCT abm.brandCategory FROM AdminBrandMaster abm")
	List<String> findDistinctBrandCategories();

	@Query("SELECT DISTINCT abm.brandCategory FROM AdminBrandMaster abm")
	List<AdminBrandMaster> findByBrandId(String brandId);

	@Query("SELECT DISTINCT abm.brandSubcategory, abm.brandId FROM AdminBrandMaster abm")
	List<Object[]> findDistinctBrandSubcategoryAndIds();

	@Query("SELECT DISTINCT abm.brandSubcategory FROM AdminBrandMaster abm")
	List<String> findDistinctBrandSubcategory();

	@Query("SELECT DISTINCT abm.brandId FROM AdminBrandMaster abm")
	List<String> findDistinctBrandIds();

}
