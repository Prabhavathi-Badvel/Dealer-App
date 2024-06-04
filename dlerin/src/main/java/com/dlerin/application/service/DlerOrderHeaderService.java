package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.entity.DlerOrderDetails;
import com.dlerin.application.entity.DlerOrderHeader;

public interface DlerOrderHeaderService {

	public DlerOrderHeader updateHeaderDetails(DlerOrderHeader header);
	public List<DlerOrderDetails> getOrderData(String orderId, String orderBy, String fromDate, String toDate,String orderTo);

}
