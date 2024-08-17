package com.dlerin.application.dto;

import java.util.List;

import com.dlerin.application.entity.DlerStoreMaterial;

import lombok.Data;

@Data
public class ResponseUpdateDlerStoreMaterialDto {

    private String message;
    private boolean status;
    private List<DlerStoreMaterial> data;
}
