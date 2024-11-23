package com.dlerin.application.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "dler_order_details")
public class DlerOrderDetails {

	@Id
	@Column(name = "line_id")
	private String lineId;

	@Column(name = "dler_id_sku_id_material_id")
	private String dlerIdMaterialId;
	@Column(name = "order_qty")
	private int orderQty;
	@Column(name = "delivered_qty")
	private int deliveredQty;
	@Column(name = "status")
	private String status;
	@Column(name = "remark")
	private String remark;
	@Column(name = "price per unit")
	private int pricePerUnit;
	@Column(name = "gst")
	private String gst;
	@Column(name = "gst_code")
	private String gstCode;
	@Column(name = "order_id")
	private String orderId;
	@Column(name = "discount")
	private int discount;

	@Column(name = "delivery_total")
	private int deliveryTotal;
	@Column(name = "order_total")
	private int orderTotal;

	@Transient
	private String dlerId;

	@Transient
	private String orderTo;

	@Transient
	private LocalDate orderDate;
}
