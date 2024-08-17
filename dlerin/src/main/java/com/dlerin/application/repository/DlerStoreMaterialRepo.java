package com.dlerin.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dlerin.application.entity.DlerStoreMaterial;

public interface DlerStoreMaterialRepo extends JpaRepository<DlerStoreMaterial, String> {
	DlerStoreMaterial findByStoreIdSkuId(String storeIdMaterailId);

	@Query("SELECT d FROM DlerStoreMaterial d WHERE (d.dlerId = :dlerId) AND (d.skuId = :skuId) AND (d.storeId = :storeId)")
	List<DlerStoreMaterial> findByDlerIdAndskuId(@Param("dlerId") String dlerId,
			@Param("skuId") String skuId,@Param("storeId") String storeId);
	
	 @Query("SELECT dmp.price " +
	           "FROM DlerStoreMaterial dsm " +
	           "JOIN DlerMaterialPrice dmp ON dmp.dlerIdMaterialId = CONCAT(dsm.dlerId, '_', dsm.skuId) " +
	           "WHERE dsm.dlerId = :dlerId AND dsm.skuId = :skuId AND dsm.storeId = :storeId")
	    String findPriceByDlerIdAndSkuIdAndStoreId(@Param("dlerId") String dlerId, 
	                                               @Param("skuId") String skuId, 
	                                               @Param("storeId") String storeId);
	 
	  List<DlerStoreMaterial> findByDlerIdOrSkuIdOrStoreId(String dlerId, String skuId, String storeId);

	List<DlerStoreMaterial> findBySkuId(String skuId);
}
