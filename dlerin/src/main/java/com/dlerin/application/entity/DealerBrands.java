package com.dlerin.application.entity;

import java.util.Date;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name="dealer_brands")
public class DealerBrands {

	
	@Id
	@Column(name="brand_id_dler_id")
	private String brandIdDlerId;
	@Column(name="brand_id")
	private String brandId;
	@Column(name="business_type")
	private String businessType;
	@Column(name="updated_by")
	private String updatedBy;
	@UpdateTimestamp
	@Column(name="updated_date")
	private Date updatedDate;

	
	
	@PrePersist
	private void prePersist() {
		this.brandIdDlerId= brandId+"_"+updatedBy;
	}
}
