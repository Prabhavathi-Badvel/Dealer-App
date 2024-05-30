package com.dlerin.application.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dlerin.application.entity.DlerProfile;

@Repository
public interface DlerProfileRepo extends JpaRepository<DlerProfile, String> {

	DlerProfile findByDlerBusinessId(String dlerBusinessId);

	DlerProfile findByDlerBusinessIdOrDlerBusinessContactNo(String dlerBusinessId, String dlerBusinessContactNo);

	List<DlerProfile> findByDlerId(String dlerId);
}