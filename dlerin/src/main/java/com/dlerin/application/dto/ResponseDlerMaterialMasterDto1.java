package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.DlerMaterialMaster;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ResponseDlerMaterialMasterDto1 {

	private String message;
	private boolean status;
	private List<DlerMaterialMaster> getData;
}

