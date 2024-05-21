package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.dto.AdminMetalMasterDto;
import com.dlerin.application.entity.AdminMetalMaster;

public interface AdminMetalMasterService {
	
	public AdminMetalMaster addMaterial(AdminMetalMaster adminMaterial);
	public AdminMetalMaster updateMaterial(AdminMetalMaster adminMaterialDto);
	public List<AdminMetalMaster> getMaterial(AdminMetalMaster metals);
}
