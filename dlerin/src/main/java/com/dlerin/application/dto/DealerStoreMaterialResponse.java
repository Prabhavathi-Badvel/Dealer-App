package com.dlerin.application.dto;

import java.util.ArrayList;
import java.util.List;

import com.dlerin.application.entity.DlerMaterialMaster;
import com.dlerin.application.entity.DlerMaterialPrice;
import com.dlerin.application.entity.DlerStoreMaterial;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DealerStoreMaterialResponse {

	private List<DlerStoreMaterial> dlerStoreMaterial = new ArrayList<DlerStoreMaterial>();
	private List<DlerMaterialMaster>dlerMaterialMasters=new ArrayList<DlerMaterialMaster>();
	private List<DlerMaterialPrice> dlerMaterialPrices = new ArrayList<DlerMaterialPrice>();
	private String Error;
	
}
