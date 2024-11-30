package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.AdminStoreVerification;
import com.dlerin.application.entity.StoreMembership;

import lombok.Data;

@Data
public class ResponseCombinedAdminStorAndStorMem {

	private String message;
	private boolean status;
	private List<AdminStoreVerification> dealerStore;
	private List<StoreMembership> dlerStoreMem;
	private String error;
}
