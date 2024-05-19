package com.dlerin.application.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.dto.ProfileDto;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerProfile;
import com.dlerin.application.exception.DlerNotFoundException;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerProfileRepo;
import com.dlerin.application.service.DlerProfileService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class DlerProfileServiceImpl implements DlerProfileService {

	@Autowired
	DlerProfileRepo dlerProfileRepo;

	@Autowired
	DlerBusinessLoginRepo dlerBusinessLoginRepo;
	
	@PersistenceContext
	private EntityManager entityManager;


	@Override
	public DlerProfile addDler(DlerProfile dProfile) throws DlerNotFoundException {
		Optional<DlerBusinessLogin> dlerOptional = dlerBusinessLoginRepo.findById(dProfile.getDlerId());
		if (dlerOptional.isPresent()) {
			return dlerProfileRepo.save(dProfile);
		} else {
			throw new DlerNotFoundException("Dler not found for given Dler ID: " + dProfile.getDlerId());
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
	public List<DlerProfile> getProfile(ProfileDto profile){
		
		Optional<DlerBusinessLogin> isExists = Optional.ofNullable(dlerBusinessLoginRepo.findByDlerUserIdOrDlerEmailIdOrDlerMobileNo(profile.getDlerId(),profile.getEmail(), profile.getMobile()));
		if(isExists.isPresent()) {
			
			Optional<List<DlerProfile>> dlerPresent = Optional.ofNullable(dlerProfileRepo.findByDlerId(isExists.get().getDlerUserId()));
			if( !dlerPresent.isEmpty()) {
				profile.setEmail(isExists.get().getDlerEmailId());
				profile.setMobile(isExists.get().getDlerMobileNo());
				return dlerPresent.get();
			}
		}
		return null;
		
	}
}
