package com.dlerin.application.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.dto.AdminGstDto;
import com.dlerin.application.entity.AdminGst;
import com.dlerin.application.entity.AdminLogin;
import com.dlerin.application.repository.AdminGstRepo;
import com.dlerin.application.repository.AdminLoginRepo;
import com.dlerin.application.service.AdminGstService;

@Service
public class AdminGstServiceImpl implements AdminGstService {

	@Autowired
	AdminGstRepo adminGstRepo;

	@Autowired
	AdminLoginRepo adminLoginRepo;

	@Override
	public AdminGst addGst(AdminGst adminGst, String email, String mobile) {
		Optional<AdminLogin> login = Optional.ofNullable(adminLoginRepo.findByEmailIdOrMobileNo(email, mobile));

		if (login.isPresent()) {
			AdminLogin adminDb = login.get();
			AdminGst existingGst = adminGstRepo.findByGstCode(adminGst.getGstCode());
			if (existingGst == null) {
				adminGst.setUpdatedBy(adminDb.getEmailId());
				return adminGstRepo.save(adminGst);
			}
		}

		return null;
	}

	@Override
	public AdminGst update(AdminGst admingst) {
		Optional<AdminGst> gstExists = Optional.ofNullable(adminGstRepo.findByGstCode(admingst.getGstCode()));
		if (gstExists.isPresent()) {
			AdminGst Db = gstExists.get();
			Db.setGstPercentage(admingst.getGstPercentage());
			return adminGstRepo.save(Db);
		}
		return null;

	}

	@Override
	public List<AdminGst> getDetails(AdminGst admingst) {

		if (admingst.getGstCode() != null && admingst.getGstPercentage() != 0) {

			List<AdminGst> details = adminGstRepo.findByGstCodeAndGstPercentage(admingst.getGstCode(),
					admingst.getGstPercentage());
			return details;
		}
		return adminGstRepo.findByGstCodeOrGstPercentage(admingst.getGstCode(), admingst.getGstPercentage());

	}
}
