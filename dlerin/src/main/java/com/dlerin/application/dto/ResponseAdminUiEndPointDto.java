package com.dlerin.application.dto;

import com.dlerin.application.entity.AdminUiEndPoint;

import lombok.Data;

@Data
public class ResponseAdminUiEndPointDto {
	private String message;
	private boolean status;
	private AdminUiEndPoint data;
}
