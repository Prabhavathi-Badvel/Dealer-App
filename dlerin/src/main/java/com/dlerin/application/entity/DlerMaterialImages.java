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
@AllArgsConstructor
@NoArgsConstructor
@Table(name="dler_material_images2")
public class DlerMaterialImages {

	@Id
	@Column(name="dlerid_materialid")
	private String dlerIdMaterialId;
	@Column(name="image_id")
	private String imageId;
	@Column(name="image_url1")
	private String imageUrl1;
	@Column(name="image_url2")
	private String imageUrl2;
	@Column(name="image_url3")
	private String imageUrl3;
	@Column(name="updated_by")
	private String updatedBy;
	@UpdateTimestamp
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="dlerId")
	private String dlerId;
	
	@Column(name="dlerBusinessStoreId")
	private String dlerBusinessStoreId;//dler_buscategory_storeid
	
	@Column(name="materialId")
	private String materialId;
	
	@PrePersist
	private void prePersist() {
		this.dlerIdMaterialId = dlerId + "_" +materialId;
		this.dlerBusinessStoreId=dlerId + "_" +dlerBusinessStoreId;
	}
	
}
