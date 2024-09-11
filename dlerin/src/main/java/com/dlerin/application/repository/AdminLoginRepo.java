package com.dlerin.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dlerin.application.dto.AdminLoginDto;
import com.dlerin.application.entity.AdminLogin;

@Repository
public interface AdminLoginRepo extends JpaRepository<AdminLogin, String> {

	AdminLoginDto save(AdminLoginDto adminLoginDto);

	Optional<AdminLogin> findByEmailId(String emailId);

	AdminLogin findByEmpId(String empId);
	
	@Query("SELECT d FROM AdminLogin d WHERE d.empId = :empId")
	Optional<AdminLogin> findByEmpIdOne(@Param("empId")String empId);

	Optional<AdminLogin> findByEmailIdOrMobileNo(String emailId, String mobileNo);

	AdminLogin findByMobileNo(String mobile);
	
	AdminLogin findByEmailIdOrMobileNoOrEmpId(String emailId, String mobileNo, String empId);

}
