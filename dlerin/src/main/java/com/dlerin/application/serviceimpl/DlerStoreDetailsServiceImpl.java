package com.dlerin.application.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.dto.DealerMasterResponse;
import com.dlerin.application.dto.DlerResponse;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerMaterialMaster;
import com.dlerin.application.entity.DlerMaterialPrice;
import com.dlerin.application.entity.DlerProfile;
import com.dlerin.application.entity.DlerStoreDetails;
import com.dlerin.application.repository.AdminBrandMasterRepo;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerMaterialMasterRepo;
import com.dlerin.application.repository.DlerMaterialPriceRepo;
import com.dlerin.application.repository.DlerProfileRepo;
import com.dlerin.application.repository.DlerStoreDetailsRepo;
import com.dlerin.application.service.DlerStoreDetailsService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class DlerStoreDetailsServiceImpl implements DlerStoreDetailsService {

	@Autowired
	private DlerStoreDetailsRepo dlerStoreDetailsRepo;

	@Autowired
	private DlerBusinessLoginRepo dlerBusinessLoginRepo;

	@Autowired
	private DlerMaterialMasterRepo dlerMaterialMasterRepo;

	@Autowired
	private DlerMaterialPriceRepo dlerMaterialPriceRepo;

	@Autowired
	private DlerProfileRepo dlerProfileRepo;

	@Autowired
	private ModelMapper modelMapper;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public DlerStoreDetails addStore(DlerStoreDetails store) {
		Optional<DlerBusinessLogin> dlerIdExists = Optional
				.ofNullable(dlerBusinessLoginRepo.findByDlerUserId(store.getDlerId()));
		if (dlerIdExists.isPresent()) {
			DlerBusinessLogin db = dlerIdExists.get();
			return dlerStoreDetailsRepo.save(store);
		}
		return null;
	}

	@Override
	public DlerStoreDetails updateStore(DlerStoreDetails store) {
		Optional<DlerStoreDetails> idExists = dlerStoreDetailsRepo.findById(store.getDlerIdStoreId());

		if (idExists.isPresent()) {
			DlerStoreDetails Db = idExists.get();
			Db.setBusinessType(store.getBusinessType());
			Db.setGst(store.getGst());
			Db.setGstDocument(store.getGstDocument());
			Db.setLocation(store.getLocation());
			Db.setTradeLicense(store.getTradeLicense());
			Db.setUpdatedBy(store.getUpdatedBy());
			Db.setDlerId(store.getDlerId());
			return dlerStoreDetailsRepo.save(Db);
		}
		return null;
	}

	@Override
	public List<DlerStoreDetails> getDlerStoreDetails(String location, String businessType, String storeId,
			String dlerId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DlerStoreDetails> query = cb.createQuery(DlerStoreDetails.class);
		Root<DlerStoreDetails> root = query.from(DlerStoreDetails.class);
		List<Predicate> predicates = new ArrayList<>();

		if (location != null) {
			predicates.add(cb.equal(root.get("location"), location));
		}
		if (businessType != null) {
			predicates.add(cb.equal(root.get("businessType"), businessType));
		}
		if (storeId != null) {
			predicates.add(cb.equal(root.get("storeId"), storeId));
		}
		if (dlerId != null) {
			predicates.add(cb.equal(root.get("dlerId"), dlerId));
		}

		query.where(predicates.toArray(new Predicate[0]));

		return entityManager.createQuery(query).getResultList();
	}

	@Override
	public List<DlerStoreDetails> getAllDlerStoreDetails() {
		return dlerStoreDetailsRepo.findAll();
	}

	@Override
	public void deleteDlerStoreDetailsById(String dlerIdStoreId) {
		dlerStoreDetailsRepo.deleteById(dlerIdStoreId);
	}

	@Override
	public List<DlerResponse> getDataBy(String businessType, String location) {
		List<DlerStoreDetails> dealerStoreDetails;

		// Fetch dealerStoreDetails by businessType and/or location
		if (businessType != null && !businessType.isEmpty() && location != null && !location.isEmpty()) {
			dealerStoreDetails = dlerStoreDetailsRepo.findByBusinessTypeAndLocation(businessType, location);
		} else if (businessType != null && !businessType.isEmpty()) {
			dealerStoreDetails = dlerStoreDetailsRepo.findByBusinessType(businessType);
		} else if (location != null && !location.isEmpty()) {
			dealerStoreDetails = dlerStoreDetailsRepo.findByLocation(location);
		} else {
			dealerStoreDetails = new ArrayList<>();
		}

		return dealerStoreDetails.stream().map(ds -> {
			List<DlerMaterialMaster> materialMasters = dlerMaterialMasterRepo.findByDlerId(ds.getDlerId());

			if (!materialMasters.isEmpty()) {
				DlerResponse response = modelMapper.map(ds, DlerResponse.class);
				response.setMaterialMasters(materialMasters);
				return response;
			} else {
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	@Override
	public DealerMasterResponse getDealerDetails(String businessType, String location, String brandId,
	        String businessName, String materialName) {
	    DealerMasterResponse dmr = new DealerMasterResponse();

	    // Check for businessName
	    List<DlerProfile> dps;
	    if (businessName != null) {
	        dps = dlerProfileRepo.findByDlerBusinessName(businessName);
	        if (dps.isEmpty()) {
	            dmr.setError("No profiles found for the provided businessName.");
	            return dmr;  // Return early if no profiles are found
	        }
	    } else {
	        dps = dlerProfileRepo.findAll();  // Fetch all profiles if no businessName is provided
	    }

	    dmr.setDlerProfiles(dps);

	    // Handle businessType and location
	    Optional<List<DlerStoreDetails>> storeDetailsOpt = Optional.ofNullable(dlerStoreDetailsRepo.findByBusinessTypeAndLocation(businessType, location));
	    List<DlerStoreDetails> dlerStoreDetails = storeDetailsOpt.orElse(Collections.emptyList());

	    if (dlerStoreDetails.isEmpty()) {
	        dmr.setError("No store details found for the provided businessType and location.");
	        return dmr;  // Return early if no store details are found
	    }

	    dmr.setDlerStoreDetails(dlerStoreDetails);

	    List<DlerMaterialMaster> allMaterialMasters = new ArrayList<>();
	    List<DlerMaterialPrice> allMaterialPrices = new ArrayList<>();

	    // Handle brandId and materialName
	    for (DlerStoreDetails ds : dlerStoreDetails) {
	        List<DlerMaterialMaster> dmms = new ArrayList<>();

	        // Check for brandId
	        if (brandId != null) {
	            dmms = Optional.ofNullable(dlerMaterialMasterRepo.findByDlerIdAndBrandId(ds.getDlerId(), brandId))
	                           .orElse(Collections.emptyList());

	            // If no materials found for the provided brandId, set error and return
	            if (dmms.isEmpty()) {
	                dmr.setError("No material found for the provided brandId.");
	                return dmr;
	            }

	            // Handle materialName filter
	            if (materialName != null) {
	                dmms = dlerMaterialMasterRepo.findByDlerIdAndBrandIdAndMaterialName(ds.getDlerId(), brandId, materialName);
	                if (dmms.isEmpty()) {
	                    dmr.setError("No material found for the provided materialName.");
	                    return dmr;
	                }
	            }
	        } else {
	            // Fetch all materials if brandId is not provided
	            dmms = Optional.ofNullable(dlerMaterialMasterRepo.findByDlerId(ds.getDlerId()))
	                           .orElse(Collections.emptyList());
	        }

	        if (!dmms.isEmpty()) {
	            allMaterialMasters.addAll(dmms);
	            dmms.forEach(dmm -> {
	                List<DlerMaterialPrice> dmp = dlerMaterialPriceRepo.findByDlerIdMaterialId(dmm.getDlerIdMaterialId());
	                if (!dmp.isEmpty()) {
	                    allMaterialPrices.addAll(dmp);
	                }
	            });
	        }
	    }

	    if (!allMaterialMasters.isEmpty()) {
	        dmr.setMaterialMasters(allMaterialMasters);
	    }
	    if (!allMaterialPrices.isEmpty()) {
	        dmr.setMaterialPrices(allMaterialPrices);
	    }
	    return dmr;
	}

}
