package com.dlerin.application.dto;

import java.util.List;
import com.dlerin.application.entity.DlerMaterialAvailability;
import lombok.Data;

@Data
public class ResponseDlerMaterialAvailabilityDto {
	
	private String message;
	private boolean status;
	private List<DlerMaterialAvailability> getData;

}
