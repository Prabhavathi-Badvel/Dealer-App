package com.dlerin.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.AdminMetalMaster;

@Repository
public interface AdminMetalMasterRepo extends JpaRepository<AdminMetalMaster, String> {

	AdminMetalMaster findByMaterialId(String materialId);
	
	@Query("SELECT DISTINCT amm.materialType FROM AdminMetalMaster amm")
	List<String> findDistinctMaterialType();

	@Query("SELECT DISTINCT amm.materialShape FROM AdminMetalMaster amm")
	List<String> findDistinctMaterialShape();

}
