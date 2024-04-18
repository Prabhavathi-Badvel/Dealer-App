package com.dlerin.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.dto.AdminMetalMasterDto;
import com.dlerin.application.entity.AdminMetalMaster;

@Repository
public interface AdminMetalMasterRepo extends JpaRepository<AdminMetalMaster, String> {

	AdminMetalMaster save(AdminMetalMasterDto adminMaterial);

	List<AdminMetalMaster> findByMaterialId(String materialId);

	List<AdminMetalMaster> findByMaterialType(String materialType);

	List<AdminMetalMaster> findByMaterialShape(String materialShape);

	List<AdminMetalMaster> findByMaterialIdOrMaterialTypeOrMaterialShape(String materialId, String materialType,
			String materialShape);
}
