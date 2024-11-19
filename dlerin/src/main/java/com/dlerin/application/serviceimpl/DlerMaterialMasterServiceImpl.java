package com.dlerin.application.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerMaterialAvailability;
import com.dlerin.application.entity.DlerMaterialMaster;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerMaterialAvailabilityRepo;
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

	@Autowired
	DlerMaterialAvailabilityRepo dlerMaterialAvailabilityRepo;

	DlerMaterialAvailability availability = new DlerMaterialAvailability();

	
	@Override
	public DlerMaterialMaster add(DlerMaterialMaster dlerMaterialMaster) {
	    Optional<DlerBusinessLogin> dlerPresent = Optional
	            .ofNullable(dlerBusinessLoginRepo.findByDlerUserId(dlerMaterialMaster.getDlerId()));

	    if (dlerPresent.isPresent()) {
	        Optional<DlerMaterialMaster> dlerIdMaterialIdExists = Optional.ofNullable(
	                dlerMaterialMasterRepo.findByDlerIdMaterialId(dlerMaterialMaster.getDlerIdMaterialId()));
	        if (!dlerIdMaterialIdExists.isPresent()) {
	            DlerMaterialMaster saved = dlerMaterialMasterRepo.save(dlerMaterialMaster);

	            DlerMaterialAvailability dlerIdMaterialAvailability = dlerMaterialAvailabilityRepo
	                    .findByDlerIdMaterialId(dlerMaterialMaster.getDlerIdMaterialId());

	            if (dlerIdMaterialAvailability == null) {
	                DlerMaterialAvailability availability = new DlerMaterialAvailability();
	                availability.setDlerId(saved.getDlerId());
	                availability.setDlerIdMaterialId(saved.getDlerIdMaterialId());
	                availability.setAvailability("Yes");
	                availability.setOnlineDisplay(null);
	                availability.setReturnPolicy(null);
	                availability.setUpdatedBy(null);
	                availability.setUpdatedDate(null);

	                dlerMaterialAvailabilityRepo.save(availability);
	            }

	            return saved;
	        }
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
	public List<DlerMaterialMaster> getDlerMaterialProfile(String brandId, String materialType, String materialId,
			String dlerId, String materialName) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DlerMaterialMaster> query = cb.createQuery(DlerMaterialMaster.class);
		Root<DlerMaterialMaster> root = query.from(DlerMaterialMaster.class);
		List<Predicate> predicates = new ArrayList<>();

		if (brandId != null) {
			predicates.add(cb.equal(root.get("brandId"), brandId));
		}
		if (materialType != null) {
			predicates.add(cb.equal(root.get("materialType"), materialType));
		}
		if (materialId != null) {
			predicates.add(cb.equal(root.get("materialId"), materialId));
		}
		if (dlerId != null) {
			predicates.add(cb.equal(root.get("dlerId"), dlerId));
		}
		if (materialName != null) {
			predicates.add(cb.equal(root.get("materialName"), materialName));
		}

		query.where(predicates.toArray(new Predicate[0]));

		return entityManager.createQuery(query).getResultList();
	}
	
	@Override
	public List<String> getDistinctMaterialName() {
		return dlerMaterialMasterRepo.getDistinctMaterialName();
	}

}
