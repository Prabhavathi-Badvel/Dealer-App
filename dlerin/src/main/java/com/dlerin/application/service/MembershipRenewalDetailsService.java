package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.entity.MembershipRenewalDetails;
import com.dlerin.application.entity.MembershipRenewalHeader;

public interface MembershipRenewalDetailsService {

	public MembershipRenewalHeader processMembershipRenewal(List<MembershipRenewalDetails> detailsList);

}
