package com.dlerin.application.entity;

import java.time.LocalDateTime;

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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin_brand_master")
public class AdminBrandMaster {

	@Id
	@Column(name = "brand_cat_subcat")
	private String brandCatSubCat;

	@Column(name = "brand_id")
	private String brandId;

	@Column(name = "brand_name")
	private String brandName;

	@Column(name = "updated_by")
	private String updatedBy;

	@UpdateTimestamp
	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	@Column(name = "brand_category")
	private String brandCategory;

	@Column(name = "brand_subcategory")
	private String brandSubcategory;
	
	@PrePersist
	private void prePersist() {
		this.brandCatSubCat = brandName + "_" + brandCategory + "_" + brandSubcategory;
	}

}
