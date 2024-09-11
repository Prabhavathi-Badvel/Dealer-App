package com.dlerin.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AdminBusinessCategoryDTO {
	private String businessCategoryId;
    private String businessCategoryName;
    private String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private String updatedDate; // Accept date as string in dd-MM-yyyy format

    private String empId;
}
