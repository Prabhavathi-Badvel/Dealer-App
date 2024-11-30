package com.dlerin.application.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerMaterialPrice;
import com.dlerin.application.entity.DlerStoreMaterial;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerMaterialPriceRepo;
import com.dlerin.application.repository.DlerStoreDetailsRepo;
import com.dlerin.application.repository.DlerStoreMaterialRepo;
import com.dlerin.application.service.DlerStoreMaterialService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class DlerStoreMaterialServiceImpl implements DlerStoreMaterialService{

	@Autowired
	private DlerStoreMaterialRepo dlerStoreMaterialRepo;

	@Autowired
	private DlerStoreDetailsRepo dlerStoreDetailsRepo;
	
	@Autowired 
	DlerMaterialPriceRepo dlerMaterialPriceRepo;
	
	@Autowired
	DlerBusinessLoginRepo dlerBusinessLoginRepo;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public DlerStoreMaterial addStoreMaterial(DlerStoreMaterial storeMaterial) {
    	Optional<DlerBusinessLogin> dlerIdExists = Optional
				.ofNullable(dlerBusinessLoginRepo.findByDlerUserId(storeMaterial.getDlerId()));
    	if (dlerIdExists.isPresent()) {
			DlerBusinessLogin db = dlerIdExists.get();
			return dlerStoreMaterialRepo.save(storeMaterial);
		}
		return null;
	}

	@Override
	public DlerStoreMaterial updateStore(DlerStoreMaterial storeMaterial) {
		Optional<DlerStoreMaterial> idExists = dlerStoreMaterialRepo.findById(storeMaterial.getStoreIdSkuId());

		if (idExists.isPresent()) {
			DlerStoreMaterial Db = idExists.get();
			Db.setDlerId(storeMaterial.getDlerId());
			Db.setSkuId(storeMaterial.getSkuId());
			Db.setStoreId(storeMaterial.getStoreId());
			Db.setUpdatedBy(storeMaterial.getUpdatedBy());
			Db.setStoreIdSkuId(storeMaterial.getStoreIdSkuId());
			Db.setDlerId(storeMaterial.getDlerId());
			return dlerStoreMaterialRepo.save(Db);
		}
		return null;
	}
	
	@Override
	public List<DlerStoreMaterial> getDlerStoreMaterial(String storeIdSkuId,String skuId, String dlerId,String storeId) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DlerStoreMaterial> query = cb.createQuery(DlerStoreMaterial.class);
		Root<DlerStoreMaterial> root = query.from(DlerStoreMaterial.class);
		List<Predicate> predicates = new ArrayList<>();
		
		if (storeIdSkuId != null) {
			predicates.add(cb.equal(root.get("storeIdSkuId"), storeIdSkuId));
		}
		if (skuId != null) {
			predicates.add(cb.equal(root.get("skuId"), skuId));
		}
		if (dlerId != null) {
			predicates.add(cb.equal(root.get("dlerId"), dlerId));
		}
		if (storeId != null) {
			predicates.add(cb.equal(root.get("storeId"), storeId));
		}
		
		query.where(predicates.toArray(new Predicate[0]));

		return entityManager.createQuery(query).getResultList();
	}

	@Override
	public List<DlerStoreMaterial> getAllDlerStoreMaterial() {
		return dlerStoreMaterialRepo.findAll();
	}

	@Override
	public void deleteDlerStoreMaterialById(String storeIdSkuId) {
		dlerStoreMaterialRepo.deleteById(storeIdSkuId);
		
	}

	@Override
	public String getPrice(String dlerId, String skuId, String storeId) {
		 DlerMaterialPrice dto= dlerMaterialPriceRepo.findByPriceUpdatedBy(dlerId);
		 return dto.getPrice();
	}

}
