package com.dlerin.application.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.dto.AdminBrandDto;
import com.dlerin.application.entity.AdminBrandMaster;
import com.dlerin.application.repository.AdminBrandMasterRepo;
import com.dlerin.application.service.AdminBrandMasterService;

@Service
public class AdminBrandMasterImpl implements AdminBrandMasterService {

	@Autowired
	AdminBrandMasterRepo admindBrandMaterRepo;

	@Override
	public AdminBrandMaster addBrand(AdminBrandMaster adminBrand) {

		if (admindBrandMaterRepo.findByBrandCatSubCat(adminBrand.getBrandCatSubCat()) == null) {
			return admindBrandMaterRepo.save(adminBrand);
		}

		return null;
	}


	
	public boolean updateBrands(AdminBrandMaster adminBrand) {
	    Optional<AdminBrandMaster> brandOpt = Optional.ofNullable(admindBrandMaterRepo.findByBrandCatSubCat(adminBrand.getBrandCatSubCat()));

	    if (brandOpt.isPresent()) {
	        AdminBrandMaster brandDb = brandOpt.get();
	        
	        brandDb.setBrandId(adminBrand.getBrandId());
	        brandDb.setBrandName(adminBrand.getBrandName());
	        brandDb.setBrandCategory(adminBrand.getBrandCategory());
	        brandDb.setBrandSubcategory(adminBrand.getBrandSubcategory());
	        
	        admindBrandMaterRepo.save(brandDb);
	         return true;
	    } else {
	        return false; 
	    }
	}

	

	@Override
	public List<AdminBrandMaster> getBrands(String brandName, String brandCategory, String brandSubcategory) {

		if (brandName != null && brandCategory != null) {

			List<AdminBrandMaster> user = admindBrandMaterRepo.findByBrandNameAndBrandCategory(brandName, brandCategory);
			return user;
		}else if(brandName != null && brandSubcategory != null) {
			List<AdminBrandMaster> user = admindBrandMaterRepo.findByBrandNameAndBrandSubcategory(brandName,brandSubcategory);
			return user;
		}else if(brandCategory != null && brandSubcategory != null) {
			List<AdminBrandMaster> user = admindBrandMaterRepo.findByBrandCategoryAndBrandSubcategory(brandCategory,brandSubcategory);
			return user;
		}else if(  brandName!=null   &&  brandCategory != null && brandSubcategory != null) {
			List<AdminBrandMaster> user = admindBrandMaterRepo.findByBrandNameAndBrandCategoryAndBrandSubcategory(brandName,brandCategory,brandSubcategory);
			return user;
		}
		return null;


	}
}
