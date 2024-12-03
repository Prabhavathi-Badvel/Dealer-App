package com.dlerin.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.MembershipRenewalHeader;

@Repository
public interface MembershipRenewalHeaderRepo extends JpaRepository<MembershipRenewalHeader, String> {

	MembershipRenewalHeader findByMembershipOrderId(String membershipOrderId);
}
