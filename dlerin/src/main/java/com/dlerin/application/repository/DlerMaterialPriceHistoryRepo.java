package com.dlerin.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dlerin.application.entity.DlerMaterialPriceHistory;

public interface DlerMaterialPriceHistoryRepo extends JpaRepository<DlerMaterialPriceHistory, String>{

}
