package com.dlerin.application.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dlerin.application.entity.DlerMaterialMaster;
import com.dlerin.application.entity.DlerOrderDetails;
import com.dlerin.application.entity.DlerOrderHeader;
import com.dlerin.application.repository.DlerMaterialMasterRepo;
import com.dlerin.application.repository.DlerOrderDetailsRepo;
import com.dlerin.application.repository.DlerOrderHeaderRepo;
import com.dlerin.application.service.DlerOrderDetailsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class DlerOrderDetailsServiceImpl implements DlerOrderDetailsService {

	@Autowired
	DlerMaterialMasterRepo dlerMaterialMasterRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	DlerOrderDetailsRepo dlerOrderDetailsRepo;

	@Autowired
	DlerOrderHeaderRepo dlerOrderHeaderRepo;

	private String generateOrderId() {
		LocalDateTime now = LocalDateTime.now();
		String year = String.valueOf(now.getYear());
		String month = String.format("%02d", now.getMonthValue());
		String day = String.format("%02d", now.getDayOfMonth());
		String hour = String.format("%02d", now.getHour());
		String minute = String.format("%02d", now.getMinute());
		String second = String.format("%02d", now.getSecond());
		String millis = String.format("%03d", now.getNano() / 1000000).substring(0, 2);
		return "Ord" + year + month + day + hour + minute + second + millis;
	}


	private String generateLineId(String generatedOrderId, int lineCounter) {
        return String.format("%s_%05d", generatedOrderId, lineCounter);
    }
	
	public DlerOrderDetails addOrderDetailsToCart(DlerOrderDetails order) {

		Optional<DlerMaterialMaster> materialOptional = Optional
				.ofNullable(dlerMaterialMasterRepo.findDlerId(order.getDlerId()));

		if (!materialOptional.isPresent()) {
			return null;
		}

		order.setStatus("Pending");

		DlerOrderDetails savedOrder = dlerOrderDetailsRepo.save(order);

		DlerOrderHeader dlerHeader = dlerOrderHeaderRepo.findByOrderId(order.getOrderId());

		if (dlerHeader == null) {

			DlerOrderHeader newHeader = new DlerOrderHeader();
			newHeader.setStatus(savedOrder.getStatus());
			newHeader.setOrderId(savedOrder.getOrderId());
			newHeader.setTotalAmount(calculateTotalPrice(order.getOrderId()));
			newHeader.setOrderBy(savedOrder.getDlerId());
			newHeader.setUpdatedBy(savedOrder.getDlerId());
			newHeader.setOrderTo(savedOrder.getOrderTo());
			dlerOrderHeaderRepo.save(newHeader);
		} else {

			dlerHeader.setTotalAmount(calculateTotalPrice(order.getOrderId()));
			dlerOrderHeaderRepo.save(dlerHeader);
		}

		return savedOrder;
	}

	public List<DlerOrderDetails> addOrders(List<DlerOrderDetails> orders) {
		List<DlerOrderDetails> addedOrders = new ArrayList<>();
		String generatedOrderId = generateOrderId();

		int lineCounter = 1;
		
		for (DlerOrderDetails order : orders) {
			try {

				order.setOrderId(generatedOrderId);
				 order.setLineId(generateLineId(generatedOrderId, lineCounter));
	                lineCounter++;
	                
	                order.setDlerId(order.getDlerId());
	                order.setOrderTo(order.getOrderTo());
				
				DlerOrderDetails addedOrder = addOrderDetailsToCart(order);
				if (addedOrder != null) {
					addedOrders.add(addedOrder);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		updateOrderHeaderTotalAmount(generatedOrderId);

		return addedOrders;
	}

	private int calculateTotalPrice(String orderId) {
		List<DlerOrderDetails> orderDetailsList = dlerOrderDetailsRepo.findByOrderId(orderId);

		int totalPrice = 0;
		for (DlerOrderDetails orderDetail : orderDetailsList) {
			try {
				int basePrice = orderDetail.getPricePerUnit();
                int orderQty = orderDetail.getOrderQty();
                int discount = orderDetail.getDiscount();

                int priceBeforeDiscount = basePrice * orderQty;
     
                int priceAfterDiscount = priceBeforeDiscount - (priceBeforeDiscount * discount / 100);

                totalPrice += priceAfterDiscount;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return totalPrice;
	}

	private void updateOrderHeaderTotalAmount(String orderId) {
		DlerOrderHeader dlerHeader = dlerOrderHeaderRepo.findByOrderId(orderId);

		if (dlerHeader != null) {
			int totalAmount = calculateTotalPrice(orderId);
			dlerHeader.setTotalAmount(totalAmount);
			dlerOrderHeaderRepo.save(dlerHeader);
		}
	}

	
	
	@Override
	public List<DlerOrderDetails> updateOrder(List<DlerOrderDetails> orders) {
	    List<DlerOrderDetails> updatedOrders = new ArrayList<>();
	    for (DlerOrderDetails order : orders) {
	        Optional<DlerOrderDetails> lineIdOptional = dlerOrderDetailsRepo.findById(order.getLineId());
	        if (lineIdOptional.isPresent()) {
	            DlerOrderDetails dbOrderDetails = lineIdOptional.get();
	            dbOrderDetails.setRemark(order.getRemark());
	            dbOrderDetails.setStatus(order.getStatus());
	            dbOrderDetails.setDeliveredQty(order.getDeliveredQty());
	            
	            DlerOrderDetails updatedOrderDetails = dlerOrderDetailsRepo.save(dbOrderDetails);
	            updatedOrders.add(updatedOrderDetails);
	            
	            
	            
	        }
	    }

	    if (!updatedOrders.isEmpty()) {
	        
	        updateInvoicedAmount(updatedOrders.get(0).getOrderId());
	    }

	    return updatedOrders;
	}

	private void updateInvoicedAmount(String orderId) {
	    Integer invoicedAmount = calculateDeliveryAmount(orderId);
	    DlerOrderHeader orderHeader = dlerOrderHeaderRepo.findByOrderId(orderId);
	    if (orderHeader != null) {
	        orderHeader.setToBeInvoicedAmount(invoicedAmount != null ? invoicedAmount : 0); // Assign default value if invoicedAmount is null
	        
	        dlerOrderHeaderRepo.save(orderHeader);
	    }
	}

	private int calculateDeliveryAmount(String orderId) {
	    List<DlerOrderDetails> orderDetailsList = dlerOrderDetailsRepo.findByOrderId(orderId);
	    int totalPrice = 0;
	    for (DlerOrderDetails orderDetail : orderDetailsList) {
	        try {
	            int basePrice = orderDetail.getPricePerUnit();
	            int deliveredQty = orderDetail.getDeliveredQty();
	            int discount = orderDetail.getDiscount();

	            int priceBeforeDiscount = basePrice * deliveredQty;
	            int priceAfterDiscount = priceBeforeDiscount - (priceBeforeDiscount * discount / 100);

	            totalPrice += priceAfterDiscount;
	        } catch (NumberFormatException e) {
	            e.printStackTrace();
	        }
	    }
	    return totalPrice;
	}

}
