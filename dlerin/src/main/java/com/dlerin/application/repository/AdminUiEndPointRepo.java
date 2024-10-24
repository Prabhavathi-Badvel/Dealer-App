package com.dlerin.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.AdminUiEndPoint;

@Repository
public interface AdminUiEndPointRepo extends JpaRepository<AdminUiEndPoint, String>{
	AdminUiEndPoint findByUpdatedBy(String updatedBy);

	AdminUiEndPoint findBySystemId(String systemId);
}
