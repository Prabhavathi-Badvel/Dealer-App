package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.entity.DlerMaterialMaster;

public interface DlerMaterialMasterService {

	public DlerMaterialMaster add(DlerMaterialMaster dlerMaterialMaster);
	public DlerMaterialMaster update(DlerMaterialMaster dlerMaterialMaster);
	public List<DlerMaterialMaster> getProfileDlerMaterial(DlerMaterialMaster dler);
}