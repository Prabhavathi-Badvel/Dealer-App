package com.dlerin.application.dto;

import java.util.ArrayList;
import java.util.List;

import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerProfile;

import lombok.Data;

@Data
public class DlerBusinessLoginReporterDto {


	public DlerBusinessLoginReporterDto(String string) {
		// TODO Auto-generated constructor stub
	}
	public DlerBusinessLoginReporterDto() {
		// TODO Auto-generated constructor stub
	}
	private List<DlerBusinessLogin>dlerBusinessLogin=new ArrayList<DlerBusinessLogin>();
	private List<DlerProfile> dlerProfiles = new ArrayList<DlerProfile>();
	private String error;
	
	
}
