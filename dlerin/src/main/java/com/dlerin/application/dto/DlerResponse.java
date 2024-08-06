package com.dlerin.application.dto;

import java.time.LocalDate;
import java.util.List;

import com.dlerin.application.entity.AdminBrandMaster;
import com.dlerin.application.entity.DlerMaterialMaster;

import lombok.Data;

@Data
public class DlerResponse {
	private String dlerIdStoreId;

	private String dlerId;

	private String  location; 

	private String businessType;
	
	private String  storeId; //relationship

	private String gst;

	private String gstDocument;

	private String tradeLicense;
	
	private LocalDate updatedDate;

	private String updatedBy;
	
    private List<DlerMaterialMaster> materialMasters;
    
}
