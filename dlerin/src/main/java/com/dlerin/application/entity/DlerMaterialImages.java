package com.dlerin.application.entity;

import java.util.Date;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name="dler_material_images")
public class DlerMaterialImages {

	@Id
	@Column(name="dler_id_material_id")
	private String dlerIdMaterialId;
	@Column(name="image_id")
	private String imageId;
	@Column(name="image_url")
	private String imageUrl;
	@Column(name="updated_by")
	private String updatedBy;
	@UpdateTimestamp
	@Column(name="updated_date")
	private Date updatedDate;
	
	
	
}
