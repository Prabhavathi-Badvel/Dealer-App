package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.AdminStoreVerification;
import com.dlerin.application.entity.DlerStoreDetails;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DealerStoreDetailResponse {

	private DlerStoreDetails dlerStoreDetails;

	private List<AdminStoreVerification> adminStoreVerification;

}
