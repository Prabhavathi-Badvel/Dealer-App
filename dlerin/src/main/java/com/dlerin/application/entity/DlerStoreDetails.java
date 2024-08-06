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
@Table(name="dealer-store-details")
public class DlerStoreDetails {
	
	@Id
	@Column(name = "dler_id_store_id",nullable = false)
	private String dlerIdStoreId;
	
	@Column(name = "dler_id")
	private String dlerId;
	
	@Column(name="location",nullable=false)
	private String  location; 
	
	@Column(name="businees_type")
	private String businessType;
	
	@Column(name="store_id")
	private String  storeId; //relationship
	
	@Column(name="gst")
	private String gst;
	
	@Column(name="gst_document",columnDefinition = "LONGTEXT")
	private String gstDocument;
	
	@Column(name="trade_license")
	private String tradeLicense;
	
	@UpdateTimestamp
	@Column(name="updated_date")
	private LocalDate updatedDate;
	
	@Column(name="updated_by")
	private String updatedBy;

	@PrePersist
	private void prePersist() {
		this.dlerIdStoreId = dlerId + "_" + storeId;
	}
		
}
