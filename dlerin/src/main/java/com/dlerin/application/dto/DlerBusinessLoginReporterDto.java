package com.dlerin.application.dto;

import java.util.ArrayList;
import java.util.List;

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
	private List<DlerBusinessLoginDtoReport>dlerBusinessLogin=new ArrayList<DlerBusinessLoginDtoReport>();
	private List<DlerProfile> dlerProfiles = new ArrayList<DlerProfile>();
	private String error;
	
	
}
