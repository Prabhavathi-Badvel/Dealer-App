package com.dlerin.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.MembershipRenewalDetails;

@Repository
public interface MembershipRenewalDetailsRepo extends JpaRepository<MembershipRenewalDetails, String> {

	List<MembershipRenewalDetails> findByMembershipOrderId(String membershipOrderId);

}
