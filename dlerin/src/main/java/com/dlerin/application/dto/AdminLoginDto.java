package com.dlerin.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginDto {

	private String empId;
	private String emailId;
	private String mobileNo;
	private String name;
	private String address;
	private String registeredDate;
	private String updatedBy;
	private String userType;

}
