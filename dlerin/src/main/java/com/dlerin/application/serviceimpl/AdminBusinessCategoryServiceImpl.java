package com.dlerin.application.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.AdminBusinessCategory;
import com.dlerin.application.entity.AdminLogin;
import com.dlerin.application.repository.AdminBusinessCategoryRepo;
import com.dlerin.application.repository.AdminLoginRepo;
import com.dlerin.application.service.AdminBusinessCategoryService;

@Service
public class AdminBusinessCategoryServiceImpl implements AdminBusinessCategoryService {

	@Autowired
	private AdminLoginRepo adminLoginRepo;

	@Autowired
	private AdminBusinessCategoryRepo adminBusinessCategoryRepo;

	@Override
	public AdminBusinessCategory addAdminBusinessCategory(AdminBusinessCategory adminBusinessCategory, String empId) {

		Optional<AdminLogin> login = adminLoginRepo.findByEmpIdOne(empId);

		if (login.isPresent()) {
			AdminLogin adminDb = login.get();
			AdminBusinessCategory existingAdminBusinessCategory = adminBusinessCategoryRepo
					.findByBusinessCategoryId(adminBusinessCategory.getBusinessCategoryId());
			if (existingAdminBusinessCategory == null) {
				adminBusinessCategory.setUpdatedBy(adminDb.getEmpId());
				
				return adminBusinessCategoryRepo.save(adminBusinessCategory);
			}
		}
		return null;
	}

	@Override
	public List<AdminBusinessCategory> getAdminBusinessCategory(String businessCategoryId,
			String businessCategoryName) {
		if (businessCategoryId != null && businessCategoryName != null) {

			List<AdminBusinessCategory> details = adminBusinessCategoryRepo
					.findByBusinessCategoryIdAndBusinessCategoryName(businessCategoryId, businessCategoryName);
			return details;
		}
		return null;
	}

	@Override
	public AdminBusinessCategory update(AdminBusinessCategory adminBusinessCategory) {
		Optional<AdminBusinessCategory> adminExists = Optional.ofNullable(
				adminBusinessCategoryRepo.findByBusinessCategoryId(adminBusinessCategory.getBusinessCategoryId()));

		if (adminExists.isPresent()) {
			AdminBusinessCategory Db = adminExists.get();
			Db.setBusinessCategoryName(adminBusinessCategory.getBusinessCategoryName());
			Db.setBusinessCategoryId(adminBusinessCategory.getBusinessCategoryId());
			return adminBusinessCategoryRepo.save(Db);
		}
		return null;
	}

}
