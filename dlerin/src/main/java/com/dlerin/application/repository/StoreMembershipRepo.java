package com.dlerin.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.StoreMembership;

@Repository
public interface StoreMembershipRepo extends JpaRepository<StoreMembership, String> {

	Optional<StoreMembership> findByStoreId(String storeId);
	List<StoreMembership> findByStoreCurrentPlan(String storeCurrentPlan);
	boolean existsByStoreIdKey(String adminStoreVerificationId);
	
	List<StoreMembership> findByStoreIdKey(String storeIdKey);

}
