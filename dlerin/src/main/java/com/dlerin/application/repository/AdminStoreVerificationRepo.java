package com.dlerin.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.AdminStoreVerification;

@Repository
public interface AdminStoreVerificationRepo extends JpaRepository<AdminStoreVerification, String>{

	Optional<AdminStoreVerification> findByAdminStoreVerificationId(String adminStoreVerificationId);

	List<AdminStoreVerification> findByStoreId(String storeId);

}
