package com.dlerin.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.DlerMaterialAvailability;

@Repository
public interface DlerMaterialAvailabilityRepo extends JpaRepository<DlerMaterialAvailability, String> {

	DlerMaterialAvailability findByDlerIdMaterialId(String dlerIdMaterialId);

}
