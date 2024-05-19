package com.dlerin.application.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.DealerBrands;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.repository.DealerBrandsRepo;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.service.DealerBrandsService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.logging.Logger;

@Service
public class DealerBrandsServiceImpl implements DealerBrandsService {

	@Autowired
	DealerBrandsRepo dealerBrandsRepo;

	@Autowired
	DlerBusinessLoginRepo dlerBusinessLoginRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public DealerBrands addBrands(DealerBrands brand) {
		Optional<DlerBusinessLogin> dlerIdExists = Optional
				.ofNullable(dlerBusinessLoginRepo.findByDlerUserId(brand.getUpdatedBy()));

		if (dlerIdExists.isPresent()) {
			DlerBusinessLogin db = dlerIdExists.get();
			return dealerBrandsRepo.save(brand);

		}
		return null;
	}

	@Override
	public DealerBrands updateBrands(DealerBrands brands) {

		Optional<DealerBrands> idExists = dealerBrandsRepo.findById(brands.getBrandIdDlerId());

		if (idExists.isPresent()) {
			DealerBrands Db = idExists.get();
			Db.setBusinessType(brands.getBusinessType());
			return dealerBrandsRepo.save(Db);
		}
		return null;

	}

	@Override
	public List<DealerBrands> getBrands(DealerBrands brands) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DealerBrands> query = cb.createQuery(DealerBrands.class);
		Root<DealerBrands> root = query.from(DealerBrands.class);
		List<Predicate> predicates = new ArrayList<>();

		if (brands.getBrandId() != null) {
			predicates.add(cb.equal(root.get("brandId"), brands.getBrandId()));
		}
		if (brands.getUpdatedBy() != null) {
			predicates.add(cb.equal(root.get("updatedBy"), brands.getUpdatedBy()));
		}
		if (brands.getBusinessType() != null) {
			predicates.add(cb.equal(root.get("businessType"), brands.getBusinessType()));
		}

		query.where(predicates.toArray(new Predicate[0]));

		return entityManager.createQuery(query).getResultList();
	}

}
