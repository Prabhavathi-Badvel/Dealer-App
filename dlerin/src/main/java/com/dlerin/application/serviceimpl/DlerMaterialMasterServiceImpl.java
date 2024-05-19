package com.dlerin.application.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerMaterialMaster;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerMaterialMasterRepo;
import com.dlerin.application.service.DlerMaterialMasterService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class DlerMaterialMasterServiceImpl implements DlerMaterialMasterService {

	@Autowired
	DlerMaterialMasterRepo dlerMaterialMasterRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	DlerBusinessLoginRepo dlerBusinessLoginRepo;

	@Override
	public DlerMaterialMaster add(DlerMaterialMaster dlerMaterialMaster) {
		Optional<DlerBusinessLogin> dlerPresent = Optional.ofNullable(dlerBusinessLoginRepo
				.findByDlerUserId(dlerMaterialMaster.getDlerId()));

		if (dlerPresent.isPresent()) {
			Optional<DlerMaterialMaster> dlerIdmaterialidExists = Optional.ofNullable(
					dlerMaterialMasterRepo.findByDlerIdMaterialId(dlerMaterialMaster.getDlerIdMaterialId()));
			if (!dlerIdmaterialidExists.isPresent())

				return dlerMaterialMasterRepo.save(dlerMaterialMaster);

		}
		return null;
	}

	@Override
	public DlerMaterialMaster update(DlerMaterialMaster dlerMaterialMaster) {
		Optional<DlerMaterialMaster> exists = Optional
				.ofNullable(dlerMaterialMasterRepo.findByDlerIdMaterialId(dlerMaterialMaster.getDlerIdMaterialId()));
		if (exists.isPresent()) {
			DlerMaterialMaster db = exists.get();
			db.setBrandId(dlerMaterialMaster.getBrandId());
			db.setMaterialDescription(dlerMaterialMaster.getMaterialDescription());
			db.setMaterialName(dlerMaterialMaster.getMaterialName());
			db.setMaterialType(dlerMaterialMaster.getMaterialType());
			db.setPackageType(dlerMaterialMaster.getPackageType());
			db.setSkuId(dlerMaterialMaster.getSkuId());
			db.setWeight(dlerMaterialMaster.getWeight());
			db.setUnit(dlerMaterialMaster.getUnit());

			return dlerMaterialMasterRepo.save(db);
		}

		return null;

	}

	@Override
	public List<DlerMaterialMaster> getProfileDlerMaterial(DlerMaterialMaster dler) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DlerMaterialMaster> query = cb.createQuery(DlerMaterialMaster.class);
		Root<DlerMaterialMaster> root = query.from(DlerMaterialMaster.class);
		List<Predicate> predicates = new ArrayList<>();

		if (dler.getBrandId() != null) {
			predicates.add(cb.equal(root.get("brandId"), dler.getBrandId()));
		}
		if (dler.getMaterialType() != null) {
			predicates.add(cb.equal(root.get("materialType"), dler.getMaterialType()));
		}
		if (dler.getMaterialId() != null) {
			predicates.add(cb.equal(root.get("materialId"), dler.getMaterialId()));
		}
		if (dler.getDlerIdMaterialId() != null) {
			predicates.add(cb.equal(root.get("dlerIdMaterialId"), dler.getDlerIdMaterialId()));
		}
		if (dler.getDlerId() != null) {
			predicates.add(cb.equal(root.get("dlerId"), dler.getDlerId()));
		}
		if (dler.getMaterialName() != null) {
			predicates.add(cb.equal(root.get("materialName"), dler.getMaterialName()));
		}

		query.where(predicates.toArray(new Predicate[0]));

		return entityManager.createQuery(query).getResultList();
	}

}
