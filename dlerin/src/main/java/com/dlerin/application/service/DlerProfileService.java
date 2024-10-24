package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.dto.ProfileDto;
import com.dlerin.application.entity.DlerProfile;
import com.dlerin.application.exception.DlerNotFoundException;

public interface DlerProfileService {

	public DlerProfile addDler(DlerProfile dProfile) throws DlerNotFoundException;
	public DlerProfile updateProfile(DlerProfile dlerProfile) throws DlerNotFoundException;
//	public List<DlerProfile> getProfile(ProfileDto profile);
	public List<DlerProfile> getProfile(String dlerBusinessId, String dlerId, String dlerBusinessName,
			String dlerBusinessLocation, String dlerBusinessContactPerson, String dlerBusinessContactNo);
}
