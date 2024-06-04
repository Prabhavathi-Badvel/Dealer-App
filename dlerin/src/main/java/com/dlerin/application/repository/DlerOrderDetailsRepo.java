package com.dlerin.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dlerin.application.entity.DlerOrderDetails;
import java.util.List;


@Repository
public interface DlerOrderDetailsRepo extends JpaRepository<DlerOrderDetails, String>{

	 List<DlerOrderDetails> findByOrderId(String orderId);

	 List<DlerOrderDetails> findByOrderIdIn(List<String> orderIds);
}
