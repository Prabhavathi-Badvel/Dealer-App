package com.dlerin.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.AdminMetalMaster;

@Repository
public interface AdminMetalMasterRepo extends JpaRepository<AdminMetalMaster, String> {

	AdminMetalMaster findByMaterialId(String materialId);

}
