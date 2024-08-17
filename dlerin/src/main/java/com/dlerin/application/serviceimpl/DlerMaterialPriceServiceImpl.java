package com.dlerin.application.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.dto.DealerStoreMaterialResponse;
import com.dlerin.application.dto.DlerMaterialPriceDto;
import com.dlerin.application.entity.DlerMaterialMaster;
import com.dlerin.application.entity.DlerMaterialPrice;
import com.dlerin.application.entity.DlerMaterialPriceHistory;
import com.dlerin.application.entity.DlerStoreMaterial;
import com.dlerin.application.repository.DlerMaterialMasterRepo;
import com.dlerin.application.repository.DlerMaterialPriceHistoryRepo;
import com.dlerin.application.repository.DlerMaterialPriceRepo;
import com.dlerin.application.repository.DlerStoreMaterialRepo;
import com.dlerin.application.service.DlerMaterialPriceService;

@Service
public class DlerMaterialPriceServiceImpl implements DlerMaterialPriceService {

	@Autowired
	private DlerMaterialPriceRepo dlerMaterialPriceRepo;

	@Autowired
	private DlerMaterialMasterRepo dlerMaterialMasterRepo;

	@Autowired
	private DlerMaterialPriceHistoryRepo dlerMaterialPriceHistoryRepo;

	@Autowired
	private DlerStoreMaterialRepo dlerStoreMaterialRepo;

	@Override
	public List<DlerMaterialPrice> addPrices(List<DlerMaterialPrice> prices) {
		List<DlerMaterialPrice> addedPrices = new ArrayList<>();
		for (DlerMaterialPrice price : prices) {
			try {
				DlerMaterialPrice addedPrice = addPrice(price);
				if (addedPrice != null) {
					addedPrices.add(addedPrice);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return addedPrices;
	}

	private DlerMaterialPrice addPrice(DlerMaterialPrice price) throws Exception {
		Optional<DlerMaterialMaster> dleridMaterialidExists = Optional
				.ofNullable(dlerMaterialMasterRepo.findByDlerIdMaterialId(price.getDlerIdMaterialId()));
		if (dleridMaterialidExists.isPresent()) {
			DlerMaterialMaster dlerDb = dleridMaterialidExists.get();
			Optional<DlerMaterialPrice> idExists = Optional
					.ofNullable(dlerMaterialPriceRepo.findByMaterialIdPriceId(price.getMaterialIdPriceId()));
			if (!idExists.isPresent()) {
				price.setCurrency("INR");
				price.setMaterialId(dlerDb.getMaterialId());
				price.setSkuId(dlerDb.getSkuId());
				price.setDlerIdMaterialId(dlerDb.getDlerIdMaterialId());
				price.setPriceUpdatedBy(dlerDb.getDlerId());
				return dlerMaterialPriceRepo.save(price);
			}
		}
		return null;
	}

	@Override
	public boolean updatePriceAndStoreHistory(DlerMaterialPrice price) {
		try {
			Optional<DlerMaterialPrice> idExists = dlerMaterialPriceRepo.findById(price.getMaterialIdPriceId());

			if (idExists.isPresent()) {
				DlerMaterialPrice existingPrice = idExists.get();

				DlerMaterialPriceHistory oldHistoryEntry = new DlerMaterialPriceHistory();
				oldHistoryEntry.setCurrency(existingPrice.getCurrency());
				oldHistoryEntry.setDiscount(existingPrice.getDiscount());
				oldHistoryEntry.setDlerIdMaterialId(existingPrice.getDlerIdMaterialId());
				oldHistoryEntry.setGstCode(existingPrice.getGstCode());
				oldHistoryEntry.setMaterialIdPriceId(existingPrice.getMaterialIdPriceId());
				oldHistoryEntry.setOrdQty(existingPrice.getOrdQty());
				oldHistoryEntry.setPrice(existingPrice.getPrice());
				oldHistoryEntry.setPriceUpdatedDate(existingPrice.getPriceUpdatedDate());
				oldHistoryEntry.setStockAvailable(existingPrice.getStockAvailable());

				dlerMaterialPriceHistoryRepo.save(oldHistoryEntry);

				existingPrice.setPrice(price.getPrice());
				existingPrice.setPriceUpdatedDate(price.getPriceUpdatedDate());

				dlerMaterialPriceRepo.save(existingPrice);

				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<DlerMaterialPrice> getPrice(DlerMaterialPriceDto dprice) {
		// Get DlerMaterialMaster entities based on the criteria
		List<DlerMaterialMaster> dlerMasters = getDlersDetails(dprice);

		if (!dlerMasters.isEmpty()) {
			// Collect dlerIdMaterialId values from the DlerMaterialMaster entities
			List<String> dlerIdMaterialIds = dlerMasters.stream().map(DlerMaterialMaster::getDlerIdMaterialId)
					.collect(Collectors.toList());

			// Fetch DlerMaterialPrice records where dlerIdMaterialId matches
			return dlerMaterialPriceRepo.findByDlerIdMaterialIdIn(dlerIdMaterialIds);
		}
		return Collections.emptyList();
	}

	@Override
	public DealerStoreMaterialResponse getDealerPriceDetails(String skuId, String dlerId, String materialId) {
	    DealerStoreMaterialResponse response = new DealerStoreMaterialResponse();

	    // Fetch DlerMaterialMaster based on dlerId and optionally materialId and skuId
	    List<DlerMaterialMaster> dlerMaterialMasters;
	    if (materialId != null && skuId != null) {
	        // If all three parameters are provided
	        dlerMaterialMasters = dlerMaterialMasterRepo.findByDlerIdAndMaterialIdAndSkuId(dlerId, materialId, skuId);
	    } else if (materialId != null) {
	        dlerMaterialMasters = dlerMaterialMasterRepo.findByDlerIdAndMaterialId(dlerId, materialId);
	    } else {
	        dlerMaterialMasters = dlerMaterialMasterRepo.findByDlerId(dlerId);
	    }

	    if (!dlerMaterialMasters.isEmpty()) {
	        response.setDlerMaterialMasters(dlerMaterialMasters);

	        // Fetch DlerStoreMaterial by matching skuId from DlerMaterialMaster
	        List<DlerStoreMaterial> dlerStoreMaterials = new ArrayList<>();
	        for (DlerMaterialMaster materialMaster : dlerMaterialMasters) {
	            String masterSkuId = materialMaster.getSkuId();

	            // If skuId is provided in the request, match it; otherwise, fetch all matching store materials
	            if (skuId == null || skuId.equals(masterSkuId)) {
	                List<DlerStoreMaterial> storeMaterials = dlerStoreMaterialRepo.findBySkuId(masterSkuId);
	                dlerStoreMaterials.addAll(storeMaterials);
	            }
	        }
	        response.setDlerStoreMaterial(dlerStoreMaterials);

	        // Fetch DlerMaterialPrice based on dlerIdMaterialId from DlerMaterialMaster
	        List<DlerMaterialPrice> dlerMaterialPrices = new ArrayList<>();
	        for (DlerMaterialMaster materialMaster : dlerMaterialMasters) {
	            String dlerIdMaterialId = materialMaster.getDlerIdMaterialId();

	            // Find matching prices
	            List<DlerMaterialPrice> materialPrices = dlerMaterialPriceRepo.findByDlerIdMaterialId(dlerIdMaterialId);
	            dlerMaterialPrices.addAll(materialPrices);
	        }
	        response.setDlerMaterialPrices(dlerMaterialPrices);
	    }

	    return response;
	}
}
