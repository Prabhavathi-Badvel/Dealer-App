package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.AdminUiEndPoint;

import lombok.Data;

@Data
public class ResponseAdminUiEndPointDto2 {
	private String message;
	private boolean status;
	private List<AdminUiEndPoint> data;
}
