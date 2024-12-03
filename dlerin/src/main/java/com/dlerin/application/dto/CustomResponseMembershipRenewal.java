package com.dlerin.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomResponseMembershipRenewal {

	private String message;
	
	private boolean status;
}
