package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.DlerUrl;

import lombok.Data;

@Data
public class ResponseUpdateDlerUrlDto {

	private String message;
	private boolean status;
	private List<DlerUrl> data;
}
