package com.dlerin.application.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerStoreMaterial;
import com.dlerin.application.entity.DlerUrl;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerUrlRepo;
import com.dlerin.application.service.DlerUrlService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class DlerUrlServiceImpl implements DlerUrlService{

	@Autowired
	private DlerUrlRepo dlerUrlRepo;
	
	@Autowired
	private DlerBusinessLoginRepo dlerBusinessLoginRepo;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public DlerUrl addDlerUrl(DlerUrl dlerUrl) {
    	Optional<DlerBusinessLogin> dlerIdExists = Optional
				.ofNullable(dlerBusinessLoginRepo.findByDlerUserId(dlerUrl.getDlerId()));
    	
    	if (dlerIdExists.isPresent()) {
			DlerBusinessLogin db = dlerIdExists.get();
			return dlerUrlRepo.save(dlerUrl);
		}
		return null;
	}


	@Override
	public DlerUrl updateDlerUrl(DlerUrl dlerUrl) {
		Optional<DlerUrl> idExists = dlerUrlRepo.findById(dlerUrl.getUiUrl());

		if (idExists.isPresent()) {
			DlerUrl Db = idExists.get();
			Db.setDlerId(dlerUrl.getDlerId());
			Db.setUpdatedBy(dlerUrl.getUpdatedBy());
			return dlerUrlRepo.save(Db);
		}
		return null;
	}
	
	@Override
	public List<DlerUrl> getDlerUrl(String uiUrl) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DlerUrl> query = cb.createQuery(DlerUrl.class);
		Root<DlerUrl> root = query.from(DlerUrl.class);
		List<Predicate> predicates = new ArrayList<>();
		
		if (uiUrl != null) {
			predicates.add(cb.equal(root.get("uiUrl"), uiUrl));
		}	
		
		query.where(predicates.toArray(new Predicate[0]));

		return entityManager.createQuery(query).getResultList();
	}

}
