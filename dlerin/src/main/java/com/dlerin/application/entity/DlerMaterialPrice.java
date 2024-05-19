package com.dlerin.application.entity;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dler_material_price")
@Builder
public class DlerMaterialPrice {

	@Id
	@Column(name = "material_id_price_id")
	private String materialIdPriceId;

	@Column(name = "dler_id_material_id")
	private String dlerIdMaterialId;

	@Column(name = "price")
	private String price;

	@UpdateTimestamp
	@Column(name = "price_updated_date")
	private String priceUpdatedDate;

	@Column(name = "price_updated_by")
	private String priceUpdatedBy;

	@Builder.Default
	@Column(name = "currency")
	private String currency = "INR";

	@Column(name = "ord_qty")
	private String ordQty;

	@Column(name = "discount")
	private String discount;

	@Column(name = "gst_code")
	private String gstCode;

	@Column(name = "stock_available")
	private String stockAvailable;

	@Transient
	private String materialId;

	@PrePersist
	private void prePersist() {
		this.materialIdPriceId = materialId + "_" + price;
	}

}
