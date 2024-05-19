package com.dlerin.application.entity;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dler_material_master")
public class DlerMaterialMaster {

	@Id
	@Column(name = "dler_id_material_id")
	private String dlerIdMaterialId;
	@Column(name = "dler_id")
	private String dlerId;
	@Column(name = "material_name")
	private String materialName;
	@Column(name = "brand_id")
	private String brandId;
	@Column(name = "material_description")
	private String materialDescription;
	@Column(name = "material_type")
	private String materialType;
	@Column(name = "material_id")
	private String materialId;
	@Column(name = "sku_id")
	private String skuId;
	@Column(name = "weight")
	private String weight;
	@Column(name = "unit")
	private String unit;
	@Column(name = "package_type")
	private String packageType;

	@PrePersist
	private void prePersist() {
		this.dlerIdMaterialId = dlerId + "_" + materialId;
		this.weight = weight + unit;
	}
}
