package com.dlerin.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.dlerin.application.entity.DlerMaterialPrice;

@Repository
public interface DlerMaterialPriceRepo extends JpaRepository<DlerMaterialPrice, String> {

//	DlerMaterialPrice findByMaterialIdPriceId(String materialIdPriceId);
	
	List<DlerMaterialPrice> findByDlerIdMaterialId(String dlerIdMaterialId);

	List<DlerMaterialPrice> findByDlerIdMaterialIdIn(List<String> dlerIdMaterialIds);
	
//	List<DlerMaterialPrice> findByPriceUpdatedBy(String priceUpdatedBy);
	
	DlerMaterialPrice findByPriceUpdatedBy(String priceUpdatedBy);
	
	@Query("SELECT p FROM DlerMaterialPrice p WHERE p.dlerIdMaterialId = :dlerIdMaterialId")
	Optional<DlerMaterialPrice> findByDlerIdMaterialIdOne(@Param("dlerIdMaterialId") String dlerIdMaterialId);

	
}
