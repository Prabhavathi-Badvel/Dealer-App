package com.dlerin.application.entity;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="dler_order_details")
public class DlerOrderDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "line_id")
	private int lineId;
	@Column(name = "material_id")
	private String materialId;
	@Column(name="order_qty")
	private String orderQty;
	@Column(name="delivered_qty")
	private String deliveredQty;
	@Column(name="status")
	private String status;
	@Column(name="remark")
	private String remark;
	@Column(name="price per unit")
	private String pricePerUnit;
	@Column(name="gst")
	private String gst;
	@Column(name="gst_code")
	private String gstCode;
	@Column(name="invoice_no")
	private String invoiceNo;
	@Column(name="order_id")
	private String orderId;
	
	@Transient
	private String dlerId;

	
}
