package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.entity.DlerProfile;
import com.dlerin.application.exception.DlerNotFoundException;

public interface DlerProfileService {

	public DlerProfile addDler(DlerProfile dProfile, String dlerId) throws DlerNotFoundException;

	public DlerProfile updateProfile(DlerProfile dlerProfile) throws DlerNotFoundException;

	public List<DlerProfile> getProfile(String dlerBusinessName, String dlerBusinessLocation,
			String dlerBusinessContactPerson, String dlerBusinessContactNo);
}
