package com.dlerin.application.service;

import java.util.List;
import com.dlerin.application.entity.DlerOrderDetails;

import jakarta.mail.MessagingException;

public interface DlerOrderDetailsService {
	
	public List<DlerOrderDetails> addOrders(List<DlerOrderDetails> orders) throws MessagingException;
	public List<DlerOrderDetails> updateOrder(List<DlerOrderDetails> orders);
}
