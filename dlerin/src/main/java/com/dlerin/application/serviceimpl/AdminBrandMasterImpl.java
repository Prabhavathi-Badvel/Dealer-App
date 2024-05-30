package com.dlerin.application.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dlerin.application.entity.AdminBrandMaster;
import com.dlerin.application.repository.AdminBrandMasterRepo;
import com.dlerin.application.service.AdminBrandMasterService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class AdminBrandMasterImpl implements AdminBrandMasterService {

	@Autowired
	AdminBrandMasterRepo admindBrandMaterRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public AdminBrandMaster addBrand(AdminBrandMaster adminBrand) {

		if (admindBrandMaterRepo.findByBrandCatSubCat(adminBrand.getBrandCatSubCat()) == null) {
			return admindBrandMaterRepo.save(adminBrand);
		}

		return null;
	}

	@Override
	public boolean updateBrands(AdminBrandMaster adminBrand) {
		Optional<AdminBrandMaster> brandOpt = Optional
				.ofNullable(admindBrandMaterRepo.findByBrandCatSubCat(adminBrand.getBrandCatSubCat()));

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

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<AdminBrandMaster> query = cb.createQuery(AdminBrandMaster.class);
		Root<AdminBrandMaster> root = query.from(AdminBrandMaster.class);
		List<Predicate> predicates = new ArrayList<>();

		if (brandName != null) {
			predicates.add(cb.equal(root.get("brandName"), brandName));
		}
		if (brandCategory != null) {
			predicates.add(cb.equal(root.get("brandCategory"), brandCategory));
		}
		if (brandSubcategory != null) {
			predicates.add(cb.equal(root.get("brandSubcategory"), brandSubcategory));
		}

		query.where(predicates.toArray(new Predicate[0]));

		return entityManager.createQuery(query).getResultList();
	}
}
