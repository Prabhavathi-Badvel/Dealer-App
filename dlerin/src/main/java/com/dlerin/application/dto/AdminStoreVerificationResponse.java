package com.dlerin.application.dto;

import java.time.LocalDate;

import com.dlerin.application.entity.VerificationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminStoreVerificationResponse {
    private String adminStoreVerificationId;
    private String storeId;
    private String dlerId;
    private VerificationStatus verificationStatus;
    private String verifcationComment;
    private String updatedBy;
    private LocalDate createdDate;
    private LocalDate updatedDate;

    // Additional fields for the response
    private String planId;
    private String expiryDate;
}

