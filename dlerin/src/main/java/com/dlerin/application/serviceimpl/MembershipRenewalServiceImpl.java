package com.dlerin.application.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.dto.CustomResponseMembershipRenewal;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.MembershipRenewalDetails;
import com.dlerin.application.entity.MembershipRenewalDetailsHistory;
import com.dlerin.application.entity.MembershipRenewalHeader;
import com.dlerin.application.entity.PlanMembership;
import com.dlerin.application.entity.StoreMembership;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.MembershipRenewalDetailsHistoryRepo;
import com.dlerin.application.repository.MembershipRenewalDetailsRepo;
import com.dlerin.application.repository.MembershipRenewalHeaderRepo;
import com.dlerin.application.repository.PlanMembershipRepo;
import com.dlerin.application.repository.StoreMembershipRepo;
import com.dlerin.application.service.MembershipRenewalService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class MembershipRenewalServiceImpl implements MembershipRenewalService {

	@Autowired
	private MembershipRenewalHeaderRepo headerRepository;

	@Autowired
	private MembershipRenewalDetailsRepo detailsRepository;

	@Autowired
	private DlerBusinessLoginRepo dlerBusinessLoginRepo;

	@Autowired
	private PlanMembershipRepo planMembershipRepository;

	@Autowired
	private StoreMembershipRepo storeMembershipRepository;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	MembershipRenewalDetailsHistoryRepo detailsHistoryRepo;

	@Override
	public MembershipRenewalHeader processMembershipRenewal(List<MembershipRenewalDetails> detailsList) {
		if (detailsList.isEmpty()) {
			throw new IllegalArgumentException("Details list cannot be empty.");
		}
		String dlerId = detailsList.get(0).getOrderPlacedBy();
		DlerBusinessLogin dlerUser = dlerBusinessLoginRepo.findByDlerUserId(dlerId);

		if (dlerUser == null) {
			throw new IllegalArgumentException("Dler ID: " + dlerId + " not found in DlerBusinessLogin.");
		}
		String generatedMembershipOrderId = generateOrderId();
		int totalOrderAmount = 0;
		int lineCounter = 1;

		for (MembershipRenewalDetails detail : detailsList) {
			List<StoreMembership> storeMembership = storeMembershipRepository.findByStoreIdKey(detail.getStoreIdKey());
			if (storeMembership.isEmpty()) {
				throw new IllegalArgumentException(
						"Store ID: " + detail.getStoreIdKey() + " not found in StoreMembership.");
			}
			PlanMembership planMembership = planMembershipRepository.findByPlanId(detail.getPlanId());
			if (planMembership == null) {
				throw new IllegalArgumentException("Plan ID: " + detail.getPlanId() + " not found in PlanMembership.");
			}
			totalOrderAmount += detail.getOrderAmount();
			String lineItemId = generateLineId(generatedMembershipOrderId, lineCounter);
			detail.setMembershipOrderIdLineItem(lineItemId);
			detail.setMembershipOrderId(generatedMembershipOrderId); // Same for all line items
			detail.setStatus("New");
			detailsRepository.save(detail);
			lineCounter++;
		}
		MembershipRenewalHeader header = new MembershipRenewalHeader();
		header.setMembershipOrderId(generatedMembershipOrderId); // Same order ID for all line items
		header.setOrderAmount(totalOrderAmount);
		header.setStatus("New");
		header.setOrderPlacedBy(dlerId);

		return headerRepository.save(header);
	}

	private String generateOrderId() {
		LocalDateTime now = LocalDateTime.now();
		String year = String.valueOf(now.getYear());
		String month = String.format("%02d", now.getMonthValue());
		String day = String.format("%02d", now.getDayOfMonth());
		String hour = String.format("%02d", now.getHour());
		String minute = String.format("%02d", now.getMinute());
		String second = String.format("%02d", now.getSecond());
		String millis = String.format("%03d", now.getNano() / 1000000).substring(0, 2);
		return "MemOrd" + year + month + day + hour + minute + second + millis;
	}

	private String generateLineId(String generatedMembershipOrderId, int lineCounter) {
		return String.format("%s_%05d", generatedMembershipOrderId, lineCounter); // Append the counter to the order ID
	}

	// update by details
	@Override
	public CustomResponseMembershipRenewal updateMembershipRenewalDetailsStatus(List<MembershipRenewalDetails> request) {
	    StringBuilder messageBuilder = new StringBuilder();
	    boolean statusUpdated = false;

	    for (MembershipRenewalDetails detail : request) {
	        MembershipRenewalDetails existingDetail = detailsRepository.findById(detail.getMembershipOrderIdLineItem())
	                .orElseThrow(() -> new IllegalArgumentException(
	                        "Order not found: " + detail.getMembershipOrderIdLineItem()));
	        MembershipRenewalDetailsHistory historyEntry = new MembershipRenewalDetailsHistory();
	        historyEntry.setMembershipOrderIdLineItem(existingDetail.getMembershipOrderIdLineItem());
	        historyEntry.setStoreIdKey(existingDetail.getStoreIdKey());
	        historyEntry.setPlanId(existingDetail.getPlanId());
	        historyEntry.setOrderDate(existingDetail.getOrderDate());
	        historyEntry.setNumberOfDays(existingDetail.getNumberOfDays());
	        historyEntry.setStatus(existingDetail.getStatus());
	        historyEntry.setMembershipOrderId(existingDetail.getMembershipOrderId());
	        historyEntry.setOrderPlacedBy(existingDetail.getOrderPlacedBy());
	        historyEntry.setOrderAmount(existingDetail.getOrderAmount());
	        detailsHistoryRepo.save(historyEntry);
	        StoreMembership storeMembership = storeMembershipRepository.findById(existingDetail.getStoreIdKey())
	                .orElseThrow(() -> new IllegalArgumentException(
	                        "StoreMembership not found for storeIdKey: " + existingDetail.getStoreIdKey()));
	        String updatedPlanId = detail.getPlanId() != null ? detail.getPlanId() : existingDetail.getPlanId();
	        Long numberOfDays = existingDetail.getNumberOfDays();
	        LocalDate orderDate = existingDetail.getOrderDate();
	        if (orderDate == null) {
	            throw new IllegalArgumentException("Order date cannot be null.");
	        }
	        LocalDate updatedExpiryDate = orderDate.plusDays(numberOfDays);  
	        String updatedExpiryDateStr = updatedExpiryDate.toString();
	        boolean updated = false;
	        if (!updatedExpiryDateStr.equals(storeMembership.getStoreExpiryDate())) {
	            storeMembership.setStoreExpiryDate(updatedExpiryDateStr); 
	            updated = true; 
	        }
	        if (!updatedPlanId.equals(storeMembership.getStoreCurrentPlan())) {
	            storeMembership.setStoreCurrentPlan(updatedPlanId);
	            updated = true;  
	        }
	        if (updated) {
	            storeMembershipRepository.save(storeMembership);
	            messageBuilder.append("StoreMembership for storeIdKey ")
	                    .append(existingDetail.getStoreIdKey())
	                    .append(" is updated with Plan ID: ").append(updatedPlanId)
	                    .append(" and Expiry Date: ").append(updatedExpiryDateStr).append(", ");
	            statusUpdated = true;
	        } else {
	            messageBuilder.append("StoreMembership for storeIdKey ")
	                    .append(existingDetail.getStoreIdKey())
	                    .append(" is already up-to-date, ");
	        }
	        if (updated) {
	            storeMembershipRepository.save(storeMembership);
	            messageBuilder.append("StoreMembership for storeIdKey ")
	                    .append(existingDetail.getStoreIdKey())
	                    .append(" is updated with Plan ID: ").append(updatedPlanId)
	                    .append(" and Expiry Date: ").append(updatedExpiryDateStr).append(", ");
	            statusUpdated = true;
	        } else {
	            messageBuilder.append("StoreMembership for storeIdKey ")
	                    .append(existingDetail.getStoreIdKey())
	                    .append(" is already up-to-date, ");
	        }
	        if (detail.getStatus() != null && !detail.getStatus().equals(existingDetail.getStatus())) {
	            existingDetail.setStatus(detail.getStatus());
	            detailsRepository.save(existingDetail);
	            messageBuilder.append("Status for MembershipRenewalDetails with order ID: ")
	                    .append(detail.getMembershipOrderIdLineItem())
	                    .append(" is updated to: ").append(detail.getStatus()).append(". ");
	            statusUpdated = true;
	        }
	    }
	    String finalMessage = messageBuilder.toString().endsWith(", ")
	            ? messageBuilder.toString().substring(0, messageBuilder.length() - 2)
	            : messageBuilder.toString();

	    return new CustomResponseMembershipRenewal(finalMessage, statusUpdated);
	}


	// update by header
	@Override
	public CustomResponseMembershipRenewal updateMembershipRenewalHeaderStatus(List<MembershipRenewalHeader> request) {
		StringBuilder messageBuilder = new StringBuilder();
		boolean statusUpdated = false;
		for (MembershipRenewalHeader detail : request) {
			MembershipRenewalHeader existingDetail = headerRepository.findById(detail.getMembershipOrderId())
					.orElseThrow(() -> new IllegalArgumentException("Order not found"));
			if (!existingDetail.getStatus().equals(detail.getStatus())) {
				messageBuilder.append("Membership Renewal number ").append(detail.getMembershipOrderId())
						.append(" is updated to ").append(detail.getStatus()).append(", ");
				statusUpdated = true;
				existingDetail.setStatus(detail.getStatus());
				headerRepository.save(existingDetail);
			} else {
				messageBuilder.append("Membership Renewal number ").append(detail.getMembershipOrderId())
						.append(" is already ").append(detail.getStatus()).append(", ");
			}
		}
		String finalMessage = messageBuilder.toString().endsWith(", ")
				? messageBuilder.toString().substring(0, messageBuilder.length() - 2)
				: messageBuilder.toString();
		return new CustomResponseMembershipRenewal(finalMessage, statusUpdated);
	}

	@Override
	public List<MembershipRenewalDetails> getMembershipRenewalDetails(String membershipOrderIdLineItem,
			String orderPlacedBy, String fromDate, String toDate, String storeIdKey) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<MembershipRenewalDetails> query = cb.createQuery(MembershipRenewalDetails.class);
		Root<MembershipRenewalDetails> root = query.from(MembershipRenewalDetails.class);

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

	@Override
	public List<MembershipRenewalHeader> getMembershipRenewalHeader(String membershipOrderId, String orderPlacedBy,
			String status, String fromDate, String toDate) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<MembershipRenewalHeader> query = cb.createQuery(MembershipRenewalHeader.class);
		Root<MembershipRenewalHeader> root = query.from(MembershipRenewalHeader.class);

		List<Predicate> predicates = new ArrayList<>();

		if (membershipOrderId != null) {
			predicates.add(cb.equal(root.get("membershipOrderId"), membershipOrderId));
		}
		if (orderPlacedBy != null) {
			predicates.add(cb.equal(root.get("orderPlacedBy"), orderPlacedBy));
		}
		if (status != null) {
			predicates.add(cb.equal(root.get("status"), status));
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
