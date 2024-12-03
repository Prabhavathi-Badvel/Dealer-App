package com.dlerin.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.MembershipRenewalDetailsHistory;

@Repository
public interface MembershipRenewalDetailsHistoryRepo extends JpaRepository<MembershipRenewalDetailsHistory, String> {

	List<MembershipRenewalDetailsHistory> findByMembershipOrderId(String membershipOrderId);

}
