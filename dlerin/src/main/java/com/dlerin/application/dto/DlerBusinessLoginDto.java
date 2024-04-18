package com.dlerin.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DlerBusinessLoginDto {

	private String dlerUserId;

	private String dlerEmailId;

	private long dlerMobileNo;

	private String dlerName;

	private String dlerRegDate;

	private String dlerEmailOtp;

	private String dlerMobileOtp;

	private String dlerEmailVerify;

	private String dlerMobileVerify;

	private String dlerStatus;

	private String dlerStatusUpdatedBy;

	private String dlerPasswordUpdatedDate;
}
