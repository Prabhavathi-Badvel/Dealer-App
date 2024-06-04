package com.dlerin.application.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dlerin.application.entity.DlerOrderHeader;


@Repository
public interface DlerOrderHeaderRepo extends JpaRepository<DlerOrderHeader, String> {

	DlerOrderHeader  findByOrderId(String orderId);

	List<DlerOrderHeader> findByOrderIdOrOrderBy(String orderId,String orderBy);

	List<DlerOrderHeader> findByOrderDateBetween(LocalDate localDate, LocalDate localDate2);

	List<DlerOrderHeader> findByOrderTo(String orderTo);

	List<DlerOrderHeader> findByOrderBy(String orderBy);

}
