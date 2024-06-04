package com.dlerin.application.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.DlerOrderDetails;
import com.dlerin.application.entity.DlerOrderHeader;
import com.dlerin.application.repository.DlerOrderDetailsRepo;
import com.dlerin.application.repository.DlerOrderHeaderRepo;
import com.dlerin.application.service.DlerOrderHeaderService;

@Service
public class DlerOrderHeaderServiceImpl implements DlerOrderHeaderService {

	@Autowired
	DlerOrderDetailsRepo dlerOrderDetailsRepo;

	@Autowired
	DlerOrderHeaderRepo dlerOrderHeaderRepo;

	@Override
	public DlerOrderHeader updateHeaderDetails(DlerOrderHeader header) {

		Optional<DlerOrderHeader> orderId = dlerOrderHeaderRepo.findById(header.getOrderId());

		if (orderId.isPresent()) {

			DlerOrderHeader db = orderId.get();
			db.setRemarks(header.getRemarks());
			db.setStatus(header.getStatus());
			return dlerOrderHeaderRepo.save(db);
		}
		return null;

	}

	@Override
	public List<DlerOrderDetails> getOrderData(String orderId, String orderBy, String fromDate, String toDate,
			String orderTo) {
		List<DlerOrderHeader> headers = new ArrayList<>();
		List<DlerOrderDetails> orderDetails = new ArrayList<>();

		if (orderId != null) {
			DlerOrderHeader header = dlerOrderHeaderRepo.findByOrderId(orderId);
			if (header != null) {
				orderDetails = dlerOrderDetailsRepo.findByOrderIdIn(Collections.singletonList(header.getOrderId()));
			}
		} else if (orderBy != null) {
			headers = dlerOrderHeaderRepo.findByOrderBy(orderBy);
			List<String> orderIds = headers.stream().map(DlerOrderHeader::getOrderId).collect(Collectors.toList());
			if (!orderIds.isEmpty()) {
				orderDetails = dlerOrderDetailsRepo.findByOrderIdIn(orderIds);
			}
		} else if (fromDate != null && toDate != null) {
			headers = dlerOrderHeaderRepo.findByOrderDateBetween(LocalDate.parse(fromDate), LocalDate.parse(toDate));
			List<String> orderIds = headers.stream().map(DlerOrderHeader::getOrderId).collect(Collectors.toList());
			if (!orderIds.isEmpty()) {
				orderDetails = dlerOrderDetailsRepo.findByOrderIdIn(orderIds);
			}
		} else if (orderTo != null) {
			headers = dlerOrderHeaderRepo.findByOrderTo(orderTo);
			List<String> orderIds = headers.stream().map(DlerOrderHeader::getOrderId).collect(Collectors.toList());
			if (!orderIds.isEmpty()) {
				orderDetails = dlerOrderDetailsRepo.findByOrderIdIn(orderIds);
			}
		}
		return orderDetails;

	}
}
