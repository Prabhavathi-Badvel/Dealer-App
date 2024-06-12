package com.dlerin.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.DlerInvoiceDetails;

@Repository
public interface DlerInvoiceDetailsRepo extends JpaRepository<DlerInvoiceDetails, String>{

}
