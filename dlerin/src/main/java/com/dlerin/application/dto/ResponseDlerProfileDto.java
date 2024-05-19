package com.dlerin.application.dto;

import java.util.List;
import com.dlerin.application.entity.DlerProfile;
import lombok.Data;

@Data
public class ResponseDlerProfileDto {
	private String message;
	private boolean status;
	private List<DlerProfile> getDlerProfile;
}
