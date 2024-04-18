package com.dlerin.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginDto {

	private String empId;

	private String emailId;

	private long mobileNo;

	private String name;

	private String address;

	private String role;

	private String registeredDate;

	private String updatedBy;

}
