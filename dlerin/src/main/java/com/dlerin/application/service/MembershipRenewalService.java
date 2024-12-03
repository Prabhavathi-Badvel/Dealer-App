package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.dto.CustomResponseMembershipRenewal;
import com.dlerin.application.entity.MembershipRenewalDetails;
import com.dlerin.application.entity.MembershipRenewalHeader;

public interface MembershipRenewalService {

	public MembershipRenewalHeader processMembershipRenewal(List<MembershipRenewalDetails> detailsList);
	
	public CustomResponseMembershipRenewal updateMembershipRenewalDetailsStatus(List<MembershipRenewalDetails> request);

	public CustomResponseMembershipRenewal updateMembershipRenewalHeaderStatus(List<MembershipRenewalHeader> request);
	
	public List<MembershipRenewalDetails> getMembershipRenewalDetails(String membershipOrderIdLineItem,
			String orderPlacedBy, String fromDate, String toDate,String storeIdKey);

	public List<MembershipRenewalHeader> getMembershipRenewalHeader(String membershipOrderId, String orderPlacedBy,
			String status, String fromDate, String toDate);

}
