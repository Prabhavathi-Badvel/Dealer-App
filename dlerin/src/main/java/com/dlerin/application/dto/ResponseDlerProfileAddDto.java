package com.dlerin.application.dto;

import com.dlerin.application.entity.DlerProfile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDlerProfileAddDto {
private String message;
private DlerProfile dlerProfile;
}
