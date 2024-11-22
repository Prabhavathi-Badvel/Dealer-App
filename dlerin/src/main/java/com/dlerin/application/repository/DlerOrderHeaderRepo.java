package com.dlerin.application.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.dlerin.application.entity.DlerOrderHeader;

@Repository
public interface DlerOrderHeaderRepo extends JpaRepository<DlerOrderHeader, String> {

	DlerOrderHeader findByOrderId(String orderId);

	@Query("SELECT h FROM DlerOrderHeader h WHERE " +
			"(:orderId IS NULL OR h.orderId = :orderId) AND " +
			"(:orderBy IS NULL OR h.orderBy = :orderBy) AND " +
			"(:orderTo IS NULL OR h.orderTo = :orderTo) AND " +
			"(:fromDate IS NULL OR :toDate IS NULL OR h.orderDate BETWEEN :fromDate AND :toDate)")
	List<DlerOrderHeader> findOrdersByFilters(@Param("orderId") String orderId,
			@Param("orderBy") String orderBy,
			@Param("orderTo") String orderTo,
			@Param("fromDate") LocalDate fromDate,
			@Param("toDate") LocalDate toDate);

	List<DlerOrderHeader> findByOrderBy(String orderBy);

	List<DlerOrderHeader> findByOrderDateBetween(LocalDate localDate, LocalDate localDate2);

	List<DlerOrderHeader> findByOrderTo(String orderTo);

}
