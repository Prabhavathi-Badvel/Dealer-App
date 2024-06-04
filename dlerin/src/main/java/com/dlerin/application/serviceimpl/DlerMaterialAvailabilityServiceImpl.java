package com.dlerin.application.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.dlerin.application.entity.DlerMaterialAvailability;
import com.dlerin.application.service.DlerMaterialAvailabilityService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class DlerMaterialAvailabilityServiceImpl implements DlerMaterialAvailabilityService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<DlerMaterialAvailability> getMaterialAvailability(String dlerId,String dlerIdMaterialId){
		 
		 
		 CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<DlerMaterialAvailability> query = cb.createQuery(DlerMaterialAvailability.class);
			Root<DlerMaterialAvailability> root = query.from(DlerMaterialAvailability.class);
			List<Predicate> predicates = new ArrayList<>();

			if (dlerId != null) {
				predicates.add(cb.equal(root.get("dlerId"), dlerId));
			}
			if (dlerIdMaterialId != null) {
				predicates.add(cb.equal(root.get("dlerIdMaterialId"), dlerIdMaterialId));
			}
			
			query.where(predicates.toArray(new Predicate[0]));

			return entityManager.createQuery(query).getResultList();
	
	
	 }
}
