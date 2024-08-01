package com.dlerin.application.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

//	public void setBrandNameList(List<String> brandName) {
//		this.brandName = String.join(",", brandName);
//	}
//
//	public List<String> getBrandNameList() {
//		return Arrays.asList(this.brandName.split(","));
//	}
//
//	@PrePersist
//	private void prePersist() {
//		updateBrandCatSubCat();
//
//	}
//
//	@PreUpdate
//	private void preUpdate() {
//		if (brandName != null || brandCategory != null || brandSubcategory != null) {
//			updateBrandCatSubCat();
//		}
//	}
//
//	private void updateBrandCatSubCat() {
//		this.brandCatSubCat = brandName + "_" + brandCategory + "_" + brandSubcategory;
//
//	}
}
