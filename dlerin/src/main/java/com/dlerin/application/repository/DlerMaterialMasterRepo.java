package com.dlerin.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.DlerMaterialMaster;

@Repository
public interface DlerMaterialMasterRepo extends JpaRepository<DlerMaterialMaster, String>{

	DlerMaterialMaster findByDlerIdMaterialId(String dlerIdMaterialId);

	DlerMaterialMaster findByMaterialNameOrMaterialIdOrSkuId(String materialName, String materialId,
			String skuId);

}
