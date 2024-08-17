package com.dlerin.application.entity;

import java.time.LocalDate;

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
@Table(name = "dler_store_material")
public class DlerStoreMaterial {

	@Id
	@Column(name = "store_id_sku_id")
	private String storeIdSkuId;
	
	@Column(name = "dler_id")
	private String dlerId;
	
	@Column(name = "sku_id")
	private String skuId;
	
	@Column(name = "store_id")
	private String storeId;
	
	@UpdateTimestamp
	@Column(name="updated_date")
	private LocalDate updatedDate;
	
	@Column(name="updated_by")
	private String updatedBy;
	
	@PrePersist
	private void prePersist() {
		this.storeIdSkuId = storeId + "_" + skuId;
	}
}

