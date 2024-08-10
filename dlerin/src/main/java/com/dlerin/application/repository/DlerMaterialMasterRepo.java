package com.dlerin.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.DlerMaterialMaster;

@Repository
public interface DlerMaterialMasterRepo extends JpaRepository<DlerMaterialMaster, String>{

	DlerMaterialMaster findByDlerIdMaterialId(String dlerIdMaterialId);

	List<DlerMaterialMaster> findByDlerIdOrMaterialNameOrMaterialIdOrSkuId(String dlerId, String materialName, String skuId,
			String materialId);

	List<DlerMaterialMaster> findByDlerId(String dlerId);

	List<DlerMaterialMaster> findByMaterialName(String materialName);

	List<DlerMaterialMaster> findBySkuId(String skuId);

	List<DlerMaterialMaster> findByMaterialId(String materialId);

	  @Query("SELECT d FROM DlerMaterialMaster d WHERE d.dlerId = :dlerId")
	    DlerMaterialMaster findDlerId(@Param("dlerId") String dlerId);

	List<DlerMaterialMaster> findByDlerIdAndBrandId(String dlerId, String brandId);

	List<DlerMaterialMaster> findByDlerIdAndBrandIdAndMaterialName(String dlerId, String brandId, String materialName);
	  
	  
}
