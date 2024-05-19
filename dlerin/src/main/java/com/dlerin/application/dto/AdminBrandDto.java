package com.dlerin.application.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminBrandDto {

	private String brandId;
	private String brandName;
	private String updatedBy;
	private LocalDateTime updatedDate;
	private String brandCatSubCat;
	private String brandCategory;
	private String brandSubcategory;
}
