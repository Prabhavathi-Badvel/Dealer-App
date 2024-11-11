package com.dlerin.application.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "store_membership")
public class StoreMembership {
	@Id
	@Column(name = "storeid")
	private String storeId;
	
	@Column(name = "storeid_key")
	private String storeIdKey;
	
	@Column(name = "store_expiry_date")
	private String storeExpiryDate;
	
	@Column(name = "store_current_plan")
	private String storeCurrentPlan;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDate updatedDate;
	
    @Enumerated(EnumType.STRING)
	private VerificationStatus verificationStatus;

	@CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDate createdDate;
	
}
