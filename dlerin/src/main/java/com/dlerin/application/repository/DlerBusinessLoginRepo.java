package com.dlerin.application.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.DlerBusinessLogin;

@Repository
public interface DlerBusinessLoginRepo extends JpaRepository<DlerBusinessLogin, String> {

	DlerBusinessLogin findByDlerUserIdOrDlerEmailIdOrDlerMobileNo(String dlerUserId, String dlerEmailId,
			String dlerMobileNo);

	DlerBusinessLogin findByDlerEmailId(String dlerEmailId);

	DlerBusinessLogin findByDlerMobileNo(String dlerMobileNo);

	DlerBusinessLogin findByDlerUserIdOrDlerEmailId(String dlerUserId, String dlerEmailId);

	DlerBusinessLogin findByDlerUserId(String userId);
	
//	DlerBusinessLogin findByDlerEmailIdOrDlerMobileNo(String dlerEmailId, String dlerMobileNo);
	
	 Optional<DlerBusinessLogin> findByDlerEmailIdOrDlerMobileNo(String dlerEmailId, String dlerMobileNo);
	 
	 List<DlerBusinessLogin> findByDlerEmailIdOrDlerUserIdOrDlerMobileNo(String dlerEmailId,String dlerUserId,
				String dlerMobileNo);
	 
	 @Query("SELECT d FROM DlerBusinessLogin d WHERE STR_TO_DATE(d.dlerRegDate, '%Y-%m-%d %H:%i:%s') BETWEEN :fromDate AND :toDate")
	 List<DlerBusinessLogin> findAllByDlerRegDateBetween(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
}
