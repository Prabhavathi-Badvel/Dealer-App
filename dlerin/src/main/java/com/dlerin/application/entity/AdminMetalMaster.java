package com.dlerin.application.entity;

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
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin_metal_master")
public class AdminMetalMaster {

	@Id
	@Column(name = "material_id")
	private String materialId;
	@Column(name = "material_type")
	private String materialType;
	@Column(name = "material_width")
	private String materialWidth;
	@Column(name = "material_length")
	private String materialLength;
	@Column(name = "material_thickness")
	private String materialThickness;
	@Column(name = "material_shape")
	private String materialShape;
	@Column(name = "width_in_units")
	private String widthInUnits;
	@Column(name = "length_in_units")
	private String lengthInUnits;
	@Column(name = "thickness_units")
	private String thicknessUnits;

	@PrePersist
	private void prePersist() {

		String subString = materialType.substring(0, Math.min(10, materialType.length()));
		String subString1 = materialShape.substring(0, Math.min(10, materialShape.length()));
		this.materialId = subString + "_" + materialWidth + "_" + materialLength + "_" + materialThickness + "_"
				+ subString1;
		this.materialWidth = materialWidth + widthInUnits;
		this.materialLength = materialLength + lengthInUnits;
		this.materialThickness = materialThickness + thicknessUnits;
	}

}
