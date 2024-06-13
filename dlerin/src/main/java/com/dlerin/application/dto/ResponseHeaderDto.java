package com.dlerin.application.dto;

import java.util.List;
import com.dlerin.application.entity.DlerOrderHeader;
import lombok.Data;

@Data
public class ResponseHeaderDto {
	private String message;
	private boolean status;
	private List<DlerOrderHeader> data;
}
