package com.dlerin.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.DlerBusinessLogin;

@Repository
public interface DlerBusinessLoginRepo extends JpaRepository<DlerBusinessLogin, String> {

	DlerBusinessLogin findByDlerUserIdOrDlerEmailIdOrDlerMobileNo(String dlerUserId, String dlerEmailId,
			String dlerMobileNo);

	DlerBusinessLogin findByDlerEmailId(String dlerEmailId);

//	DlerBusinessLogin findByDlerEmailIdOrDlerMobileNo(String dlerEmailId, String dlerMobileNo);

	DlerBusinessLogin findByDlerMobileNo(String dlerMobileNo);

	DlerBusinessLogin findByDlerUserIdOrDlerEmailId(String dlerUserId, String dlerEmailId);

	DlerBusinessLogin findByDlerUserId(String userId);
	
	DlerBusinessLogin findByDlerEmailIdOrDlerMobileNo(String dlerEmailId, String dlerMobileNo);
}
