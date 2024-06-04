package com.dlerin.application.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dlerin.application.entity.DlerMaterialPriceHistory;
import com.dlerin.application.service.DlerMaterialPriceHistoryService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
@Service
public class DlerMaterialPriceHistoryServiceImpl implements DlerMaterialPriceHistoryService  {
	
	@PersistenceContext
	private EntityManager entityManager;

	
	
	@Override
	public List<DlerMaterialPriceHistory> getPriceHistory(String materialIdPriceId, String dlerIdMaterialId, String id,
			String dlerId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DlerMaterialPriceHistory> query = cb.createQuery(DlerMaterialPriceHistory.class);
		Root<DlerMaterialPriceHistory> root = query.from(DlerMaterialPriceHistory.class);
		List<Predicate> predicates = new ArrayList<>();

		if (materialIdPriceId != null) {
			predicates.add(cb.equal(root.get("materialIdPriceId"), materialIdPriceId));
		}
		if (dlerIdMaterialId != null) {
			predicates.add(cb.equal(root.get("dlerIdMaterialId"), dlerIdMaterialId));
		}
		if (id != null) {
			predicates.add(cb.equal(root.get("id"), id));
		}
		if (dlerId != null) {
			predicates.add(cb.equal(root.get("dlerId"), dlerId));
		}
		

		query.where(predicates.toArray(new Predicate[0]));

		return entityManager.createQuery(query).getResultList();
	}
}
