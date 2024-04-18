package com.dlerin.application.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerProfile;
import com.dlerin.application.exception.DlerNotFoundException;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerProfileRepo;
import com.dlerin.application.service.DlerProfileService;

@Service
public class DlerProfileServiceImpl implements DlerProfileService {

	@Autowired
	DlerProfileRepo dlerProfileRepo;

	@Autowired
	DlerBusinessLoginRepo dlerBusinessLoginRepo;

	@Override
	public DlerProfile addDler(DlerProfile dProfile, String dlerId) throws DlerNotFoundException {
		Optional<DlerBusinessLogin> dlerOptional = dlerBusinessLoginRepo.findById(dlerId);
		if (dlerOptional.isPresent()) {
			return dlerProfileRepo.save(dProfile);
		} else {
			throw new DlerNotFoundException("Dler not found for given Dler ID: " + dlerId);
		}
	}

	@Override
	public DlerProfile updateProfile(DlerProfile dlerProfile) throws DlerNotFoundException {
		{
			Optional<DlerProfile> businessIdExists = Optional
					.ofNullable(dlerProfileRepo.findByDlerBusinessId(dlerProfile.getDlerBusinessId()));
			if (businessIdExists.isPresent()) {
				DlerProfile dpDb = businessIdExists.get();

				dpDb.setDlerBusinessContactNo(dlerProfile.getDlerBusinessContactNo());
				dpDb.setDlerBusinessContactPerson(dlerProfile.getDlerBusinessContactPerson());
				dpDb.setDlerBusinessLocation(dlerProfile.getDlerBusinessLocation());
				dpDb.setDlerBusinessName(dlerProfile.getDlerBusinessName());

				return dlerProfileRepo.save(dpDb);
			} else {

				throw new DlerNotFoundException("Dler not found for given Dler ID: " + dlerProfile.getDlerBusinessId());
			}
		}

	}

	@Override
	public List<DlerProfile> getProfile( String dlerBusinessName,String dlerBusinessLocation,String dlerBusinessContactPerson,
			String dlerBusinessContactNo) {

		return dlerProfileRepo.findByDlerBusinessNameOrDlerBusinessLocationOrDlerBusinessContactPersonOrDlerBusinessContactNo(dlerBusinessName,
				dlerBusinessLocation, dlerBusinessContactPerson, dlerBusinessContactNo);

	}

}
