package com.dlerin.application.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.dto.DlerMaterialPriceDto;
import com.dlerin.application.entity.DlerMaterialMaster;
import com.dlerin.application.entity.DlerMaterialPrice;
import com.dlerin.application.entity.DlerMaterialPriceHistory;
import com.dlerin.application.repository.DlerMaterialMasterRepo;
import com.dlerin.application.repository.DlerMaterialPriceHistoryRepo;
import com.dlerin.application.repository.DlerMaterialPriceRepo;
import com.dlerin.application.service.DlerMaterialPriceService;

@Service
public class DlerMaterialPriceServiceImpl implements DlerMaterialPriceService {

	@Autowired
	DlerMaterialPriceRepo dlerMaterialPriceRepo;

	@Autowired
	DlerMaterialMasterRepo dlerMaterialMasterRepo;

	@Autowired
	DlerMaterialPriceHistoryRepo dlerMaterialPriceHistoryRepo;

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
	public List<DlerMaterialPrice> get(DlerMaterialPriceDto dprice) {

		Optional<DlerMaterialMaster> exists = Optional
				.ofNullable(dlerMaterialMasterRepo.findByMaterialNameOrMaterialIdOrSkuId(dprice.getMaterialName(),
						dprice.getSkuId(), dprice.getMaterialId()));
		if (exists.isPresent()) {
			Optional<List<DlerMaterialPrice>> isdlerIdMaterialIdExists = Optional
					.ofNullable(dlerMaterialPriceRepo.findByDlerIdMaterialId(exists.get().getDlerIdMaterialId()));
			if (!isdlerIdMaterialIdExists.isEmpty()) {
				return isdlerIdMaterialIdExists.get();
			}
		}
		return null;

	}
}
