package com.dlerin.application.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.dto.ResponseCombinedDealerBrandsDto;
import com.dlerin.application.entity.AdminBrandMaster;
import com.dlerin.application.entity.DealerBrands;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.repository.AdminBrandMasterRepo;
import com.dlerin.application.repository.DealerBrandsRepo;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.service.DealerBrandsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class DealerBrandsServiceImpl implements DealerBrandsService {

	@Autowired
	DealerBrandsRepo dealerBrandsRepo;

	@Autowired
	DlerBusinessLoginRepo dlerBusinessLoginRepo;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private AdminBrandMasterRepo adminBrandMasterRepo;

	@Override
	public DealerBrands addBrands(DealerBrands brand) {
		Optional<DlerBusinessLogin> dlerIdExists = Optional
				.ofNullable(dlerBusinessLoginRepo.findByDlerUserId(brand.getUpdatedBy()));

		if (dlerIdExists.isPresent()) {
			DlerBusinessLogin db = dlerIdExists.get();
			return dealerBrandsRepo.save(brand);

		}
		return null;
	}

	@Override
	public DealerBrands updateBrands(DealerBrands brands) {

		Optional<DealerBrands> idExists = dealerBrandsRepo.findById(brands.getBrandIdDlerId());

		if (idExists.isPresent()) {
			DealerBrands Db = idExists.get();
			Db.setBusinessType(brands.getBusinessType());
			return dealerBrandsRepo.save(Db);
		}
		return null;

	}

	@Override
	public List<DealerBrands> getBrands(String brandId, String updatedBy, String businessType) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DealerBrands> query = cb.createQuery(DealerBrands.class);
		Root<DealerBrands> root = query.from(DealerBrands.class);
		List<Predicate> predicates = new ArrayList<>();

		if (brandId != null) {
			predicates.add(cb.equal(root.get("brandId"), brandId));
		}
		if (updatedBy != null) {
			predicates.add(cb.equal(root.get("updatedBy"), updatedBy));
		}
		if (businessType != null) {
			predicates.add(cb.equal(root.get("businessType"), businessType));
		}

		query.where(predicates.toArray(new Predicate[0]));

		return entityManager.createQuery(query).getResultList();
	}
	
	 @Override
	    public ResponseCombinedDealerBrandsDto getBrandsAndAdmin(String brandId, String updatedBy, String businessType) {
	        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	        CriteriaQuery<DealerBrands> query = cb.createQuery(DealerBrands.class);
	        Root<DealerBrands> root = query.from(DealerBrands.class);
	        List<Predicate> predicates = new ArrayList<>();

	        if (brandId != null) {
	            predicates.add(cb.equal(root.get("brandId"), brandId));
	        }
	        if (updatedBy != null) {
	            predicates.add(cb.equal(root.get("updatedBy"), updatedBy));
	        }
	        if (businessType != null) {
	            predicates.add(cb.equal(root.get("businessType"), businessType));
	        }

	        query.where(predicates.toArray(new Predicate[0]));
	        List<DealerBrands> dealerBrandsList = entityManager.createQuery(query).getResultList();

	        List<AdminBrandMaster> adminBrandMasterList = new ArrayList<>();
	        if (brandId != null) {
	            adminBrandMasterList = adminBrandMasterRepo.findByBrandId(brandId);
	        }

	        ResponseCombinedDealerBrandsDto response = new ResponseCombinedDealerBrandsDto();
	        if (!dealerBrandsList.isEmpty() || !adminBrandMasterList.isEmpty()) {
	            response.setMessage("Dealer and Admin Brand details found.");
	            response.setStatus(true);
	            response.setDealerBrands(dealerBrandsList);
	            response.setAdminBrandMaster(adminBrandMasterList);
	        } else {
	            response.setMessage("No records found for the provided parameters.");
	            response.setStatus(false);
	        }

	        return response;
	    }
}
