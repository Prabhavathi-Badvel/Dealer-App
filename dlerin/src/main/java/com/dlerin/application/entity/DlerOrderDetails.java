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
	@Column(name = "line_id")
	private String lineId;
	@Column(name = "material_id")
	private String materialId;
	@Column(name="order_qty")
	private int orderQty;
	@Column(name="delivered_qty")
	private int deliveredQty;
	@Column(name="status")
	private String status;
	@Column(name="remark")
	private String remark;
	@Column(name="price per unit")
	private int pricePerUnit;
	@Column(name="gst")
	private String gst;
	@Column(name="gst_code")
	private String gstCode;
	@Column(name="order_id")
	private String orderId;
	@Column(name="discount")
	private int discount;
	
	@Transient
	private String dlerId;

	@Transient
	private String orderTo;
	
}
