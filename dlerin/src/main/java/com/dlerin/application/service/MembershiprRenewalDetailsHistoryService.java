package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.entity.MembershipRenewalDetailsHistory;



public interface MembershiprRenewalDetailsHistoryService {

	public List<MembershipRenewalDetailsHistory> getMembershiprRenewalDetailsHistory(String membershipOrderIdLineItem,
			String orderPlacedBy, String fromDate, String toDate, String storeIdKey);
}
