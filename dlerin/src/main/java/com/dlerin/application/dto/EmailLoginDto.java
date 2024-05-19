package com.dlerin.application.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailLoginDto {
    private String email;
    private String password;
    private String mobile;
    private String otp;
}
