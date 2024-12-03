package com.dlerin.application.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "membership_renewal_details_history1")
public class MembershipRenewalDetailsHistory {

	@Id
	@Column(name = "membership_orderid_line_item")
	private String membershipOrderIdLineItem;
	
	@UpdateTimestamp
	@Column(name="order_date")
	private LocalDate orderDate;
	
	@Column(name = "order_amount")
	private int orderAmount;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "order_placed_by")
	private String orderPlacedBy;
	
	@Column(name = "storeid")
	private String storeIdKey;

	@Column(name = "planid")
	private String planId;
	
	@Column(name = "membership_order_id")
	private String membershipOrderId;
	
	@Column(name = "number_of_days")
	private long numberOfDays;

}
