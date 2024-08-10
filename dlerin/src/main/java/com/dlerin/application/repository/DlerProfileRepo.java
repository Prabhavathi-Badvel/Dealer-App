package com.dlerin.application.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.dlerin.application.entity.DlerProfile;

@Repository
public interface DlerProfileRepo extends JpaRepository<DlerProfile, String> {

	DlerProfile findByDlerBusinessId(String dlerBusinessId);

	DlerProfile findByDlerBusinessIdOrDlerBusinessContactNo(String dlerBusinessId, String dlerBusinessContactNo);

	List<DlerProfile> findByDlerId(String dlerId);
	
	List<DlerProfile> findByDlerBusinessName(String dlerBusinessName);

	@Query("SELECT dp FROM DlerProfile dp WHERE dp.dlerId = :dlerId AND dp.dlerBusinessName = :dlerBusinessName")
    List<DlerProfile> findByDlerIdAndDlerBusinessName(@Param("dlerId") String dlerId, @Param("dlerBusinessName") String dlerBusinessName);
}