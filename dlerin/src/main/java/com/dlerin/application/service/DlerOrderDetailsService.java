package com.dlerin.application.service;

import java.util.List;
import com.dlerin.application.entity.DlerOrderDetails;

public interface DlerOrderDetailsService {
	
	public List<DlerOrderDetails> addOrders(List<DlerOrderDetails> orders);
	public List<DlerOrderDetails> updateOrder(List<DlerOrderDetails> orders);
}
