package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.dto.AdminMetalMasterDto;
import com.dlerin.application.entity.AdminMetalMaster;

public interface AdminMetalMasterService {

	public AdminMetalMasterDto addMaterial(AdminMetalMasterDto adminMaterial);
	public AdminMetalMaster updateMaterial(AdminMetalMaster adminMaterialDto);
	public List<AdminMetalMaster> getMaterial(String materialId, String materialType, String materialShape);
}
