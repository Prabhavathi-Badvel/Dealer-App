package com.dlerin.application.entity;

import java.util.Random;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
@Table(name ="dler_material_price_history")
public class DlerMaterialPriceHistory {

	@Id
	@Column(name ="id")
	private String id;
	
	@Column(name="material_id_price_id")
	private String materialIdPriceId;
	
	@Column(name="dler_id_sku_id_material_id")
	private String dlerIdMaterialId;
	
	@Column(name="price")
	private String price;
	
	@UpdateTimestamp
	@Column(name="price_updated_date")
	private String priceUpdatedDate;
	
	
	@Column(name="currency")
	private String currency;
	
	@Column(name="old_qty")
	private String ordQty;
	
	@Column(name="discount")
	private String discount;
	
	@Column(name="gst_code")
	private String gstCode;
	
	@Column(name="stock_available")
	private String stockAvailable;
	
	@PrePersist
	private void prePersist() {
		long sequenceNumber = generateRandomSixDigitNumber();
		this.id ="DPH"+ sequenceNumber;
		
	}

	private static final Random random = new Random();

	private long generateRandomSixDigitNumber() {

		long randomLong = random.nextLong(9000000L) + 1000000L;
		return randomLong;
	}

}

