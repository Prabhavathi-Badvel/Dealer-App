package com.dlerin.application.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dlerin.application.entity.MembershipRenewalDetails;
import com.dlerin.application.entity.MembershipRenewalDetailsHistory;
import com.dlerin.application.service.MembershiprRenewalDetailsHistoryService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class MembershiprRenewalDetailsHistoryServiceImpl implements MembershiprRenewalDetailsHistoryService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<MembershipRenewalDetailsHistory> getMembershiprRenewalDetailsHistory(String membershipOrderIdLineItem,
			String orderPlacedBy, String fromDate, String toDate, String storeIdKey) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<MembershipRenewalDetailsHistory> query = cb.createQuery(MembershipRenewalDetailsHistory.class);
		Root<MembershipRenewalDetailsHistory> root = query.from(MembershipRenewalDetailsHistory.class);

		List<Predicate> predicates = new ArrayList<>();

		if (membershipOrderIdLineItem != null) {
			predicates.add(cb.equal(root.get("membershipOrderIdLineItem"), membershipOrderIdLineItem));
		}
		if (orderPlacedBy != null) {
			predicates.add(cb.equal(root.get("orderPlacedBy"), orderPlacedBy));
		}
		if (storeIdKey != null) {
			predicates.add(cb.equal(root.get("storeIdKey"), storeIdKey));
		}
		if (fromDate != null && toDate != null) {
			LocalDate fromLocalDate = LocalDate.parse(fromDate);
			LocalDate toLocalDate = LocalDate.parse(toDate);
			predicates.add(cb.between(root.get("orderDate"), fromLocalDate, toLocalDate));
		} else if (fromDate != null) {
			LocalDate fromLocalDate = LocalDate.parse(fromDate);
			predicates.add(cb.greaterThanOrEqualTo(root.get("orderDate"), fromLocalDate));
		} else if (toDate != null) {
			LocalDate toLocalDate = LocalDate.parse(toDate);
			predicates.add(cb.lessThanOrEqualTo(root.get("orderDate"), toLocalDate));
		}
		query.where(predicates.toArray(new Predicate[0]));
		return entityManager.createQuery(query).getResultList();
	}

}
