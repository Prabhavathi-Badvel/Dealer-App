package com.dlerin.application.dto;

import java.util.ArrayList;
import java.util.List;

import com.dlerin.application.entity.DlerMaterialMaster;
import com.dlerin.application.entity.DlerMaterialPrice;
import com.dlerin.application.entity.DlerProfile;
import com.dlerin.application.entity.DlerStoreDetails;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DealerMasterResponse {

	private List<DlerStoreDetails> dlerStoreDetails = new ArrayList<DlerStoreDetails>();

	private List<DlerMaterialMaster> materialMasters = new ArrayList<DlerMaterialMaster>();

	private List<DlerMaterialPrice> materialPrices = new ArrayList<DlerMaterialPrice>();

	private List<DlerProfile> dlerProfiles = new ArrayList<DlerProfile>();

	private String error;
	

}
