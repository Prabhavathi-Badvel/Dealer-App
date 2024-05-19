package com.dlerin.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.AdminBrandMaster;

@Repository
public interface AdminBrandMasterRepo extends JpaRepository<AdminBrandMaster, String>{

	AdminBrandMaster findByBrandCatSubCat(String brandCatSubCat);


}
