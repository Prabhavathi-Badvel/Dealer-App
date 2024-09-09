package com.dlerin.application.service;

import java.util.List;
import com.dlerin.application.entity.AdminMetalMaster;

public interface AdminMetalMasterService {

	public AdminMetalMaster addMaterial(AdminMetalMaster adminMaterial);

	public AdminMetalMaster updateMaterial(AdminMetalMaster adminMaterialDto);

	public List<AdminMetalMaster> getMaterial(String materialId, String materialType, String materialShape);

	public List<String> getDistinctMaterialType();

	public List<String> getDistinctMaterialShape();
}
