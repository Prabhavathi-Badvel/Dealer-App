package com.dlerin.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.dto.DlerMaterialPriceDto;
import com.dlerin.application.entity.DlerMaterialPrice;

@Repository
public interface DlerMaterialPriceRepo extends JpaRepository<DlerMaterialPrice, String> {

	DlerMaterialPrice findByMaterialIdPriceId(String materialIdPriceId);

	List<DlerMaterialPrice> findByDlerIdMaterialId(String dlerIdMaterialId);

}
