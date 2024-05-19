package com.dlerin.application.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminGstDto {
	
	private String gstCode;
	private int gstPercentage;
	private Date updatedDate;
	private String updatedBy;
	private String emailId;
	private String mobileNo;
}
