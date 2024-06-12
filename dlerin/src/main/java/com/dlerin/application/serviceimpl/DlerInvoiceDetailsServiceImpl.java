package com.dlerin.application.serviceimpl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dlerin.application.entity.DlerInvoiceDetails;
import com.dlerin.application.entity.DlerOrderHeader;
import com.dlerin.application.repository.DlerInvoiceDetailsRepo;
import com.dlerin.application.repository.DlerOrderHeaderRepo;
import com.dlerin.application.service.DlerInvoiceDetailsService;

@Service
public class DlerInvoiceDetailsServiceImpl implements DlerInvoiceDetailsService {

	@Autowired
	DlerOrderHeaderRepo dlerOrderHeaderRepo;

	@Autowired
	DlerInvoiceDetailsRepo InvoiceRepo;

	@Override
	public DlerInvoiceDetails saveInvoceDetails(DlerInvoiceDetails details) {

		Optional<DlerOrderHeader> orderIdExists = Optional
				.ofNullable(dlerOrderHeaderRepo.findByOrderId(details.getOrderId()));

		if (orderIdExists.isPresent()) {
			DlerOrderHeader db = orderIdExists.get();

			details.setInviceTo(db.getOrderTo());
			details.setUpdateBy(db.getUpdatedBy());
			details.setOrderId(db.getOrderId());
			details.setTotalAmount(db.getTotalAmount());
			return InvoiceRepo.save(details);
		}
		return null;

	}
}
