package com.dlerin.application.entity;

import java.time.LocalDate;
import org.hibernate.annotations.CreationTimestamp;
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
@Table(name = "dler_order_header")
public class DlerOrderHeader {

	@Id
	@Column(name = "order_id")
	private String orderId;
	@CreationTimestamp
	@Column(name = "order_date")
	private LocalDate orderDate;
	@Column(name = "total_amount")
	private int totalAmount;
	@Column(name = "status")
	private String status;
	@Column(name = "updated_by")
	private String updatedBy;
	@Column(name = "order_to")
	private String orderTo;
	@UpdateTimestamp
	@Column(name = "updated_date")
	private String updatedDate;
	@Column(name = "remarks")
	private String remarks;
	@Column(name = "order_by")
	private String orderBy;
	@Column(name = "invoice_no")
	private String invoiceNo;
	@Column(name = "to_be_invoiced_amount")
	private int toBeInvoicedAmount;

}
