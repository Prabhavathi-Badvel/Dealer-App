package com.dlerin.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dlerin.application.entity.DlerStoreDetails;

public interface DlerStoreDetailsRepo extends JpaRepository<DlerStoreDetails, String> {
	DlerStoreDetails findByDlerIdStoreId(String dlerIdStoreId);

	DlerStoreDetails findByStoreId(String storeId);

	@Query("SELECT d FROM DlerStoreDetails d WHERE (d.location = :location) OR (d.businessType = :businessType)OR (d.storeId = :storeId)OR(d.dlerId = :dlerId) ")

	List<DlerStoreDetails> findByLocationAndBusinessType(@Param("location") String location,
			@Param("businessType") String businessType, @Param("storeId") String storeId,
			@Param("dlerId") String dlerId);

	List<DlerStoreDetails> findByBusinessType(String businessType);

	List<DlerStoreDetails> findByBusinessTypeAndLocation(String businessType, String location);

	List<DlerStoreDetails> findByLocation(String location);

	@Query("SELECT d FROM DlerStoreDetails d WHERE (d.dlerId = :dlerId) AND (d.storeId = :storeId)")
	Optional<DlerStoreDetails> findByDlerIdAndStoreId(String dlerId, String storeId);

	@Query("SELECT DISTINCT dsd.location FROM DlerStoreDetails dsd")
	List<String> findLocation();

}
