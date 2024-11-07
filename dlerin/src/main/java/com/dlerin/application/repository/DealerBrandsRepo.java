package com.dlerin.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.DealerBrands;

@Repository
public interface DealerBrandsRepo extends JpaRepository<DealerBrands, String> {

	DealerBrands findByBrandIdDlerId(String brandIdDlerId);

	boolean existsByBrandId(String brandId);

	List<DealerBrands> findByUpdatedByOrBusinessTypeOrBrandId(String updatedBy, String businessType, String brandId);
}
