package com.dlerin.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DlerOrderDetailsDto {

	private String orderId;
	private String materialId;
	private String orderBy;
	private String fromDate;
	private String toDate;
	private String orderTo;

}
