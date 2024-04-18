package com.dlerin.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.DlerBusinessLogin;

@Repository
public interface DlerBusinessLoginRepo extends JpaRepository<DlerBusinessLogin, String> {

	DlerBusinessLogin findByDlerUserIdOrDlerEmailIdOrDlerMobileNo(String dlerUserId, String dlerEmailId,
			long dlerMobileNo);

	DlerBusinessLogin findByDlerEmailId(String dlerEmailId);

	DlerBusinessLogin findByDlerEmailIdOrDlerMobileNo(String dlerEmailId, long dlerMobileNo);

	List<DlerBusinessLogin> findByDlerMobileNo(Long dlerMobileNo);

	List<DlerBusinessLogin> findByDlerUserId(String dlerUserId);

	List<DlerBusinessLogin> findByDlerName(String dlerName);

	DlerBusinessLogin findByDlerUserIdOrDlerEmailIdOrDlerMobileNoOrDlerName(String dlerUserId, String dlerEmailId,
			Long dlerMobileNo, String dlerName);

}
