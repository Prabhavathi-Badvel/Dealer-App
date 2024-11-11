package com.dlerin.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DlerBusinessLoginDtoReport {

	private String dlerUserId;

	private String dlerEmailId;

	private String dlerMobileNo;

	private String dlerName;

	private String dlerRegDate;

	private String dlerEmailVerify;

	private String dlerMobileVerify;

	private String dlerStatus;

	private String dlerStatusUpdatedBy;

	private String userType;

	public DlerBusinessLoginDtoReport(String dlerUserId, String dlerEmailId, String dlerMobileNo, String dlerName,
			String dlerRegDate, String dlerEmailVerify, String dlerMobileVerify, String dlerStatus,
			String dlerStatusUpdatedBy, String userType) {
		super();
		this.dlerUserId = dlerUserId;
		this.dlerEmailId = dlerEmailId;
		this.dlerMobileNo = dlerMobileNo;
		this.dlerName = dlerName;
		this.dlerRegDate = dlerRegDate;
		this.dlerEmailVerify = dlerEmailVerify;
		this.dlerMobileVerify = dlerMobileVerify;
		this.dlerStatus = dlerStatus;
		this.dlerStatusUpdatedBy = dlerStatusUpdatedBy;
		this.userType = userType;
	}
	
	

}
