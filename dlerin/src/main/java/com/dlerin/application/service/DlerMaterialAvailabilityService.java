package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.entity.DlerMaterialAvailability;

public interface DlerMaterialAvailabilityService {

	public List<DlerMaterialAvailability> getMaterialAvailability(String dlerId,String dlerIdMaterialId);
}
