package com.dlerin.application.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "admin_store_verification")
public class AdminStoreVerification {

	@Id
	@Column(name = "admin_store_verification_id")
	private String adminStoreVerificationId;

	@Column(name = "store_id")
	private String storeId;

	@Column(name = "dler_id")
	private String dlerId;

	@Enumerated(EnumType.STRING)
	@Column
	private VerificationStatus verificationStatus;

	@Column(name = "veriifcation_comment")
	private String verifcationComment;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "created_date", updatable = false)
	private LocalDate createdDate;

	@Column(name = "updated_date")
	private LocalDate updatedDate;

	@PrePersist
	private void onCreate() {
		if (this.adminStoreVerificationId == null) {
			this.adminStoreVerificationId = dlerId + "_" + storeId;
		}
		this.createdDate = LocalDate.now();
		this.updatedDate = LocalDate.now();
	}

	@PreUpdate
	private void onUpdate() {
		this.updatedDate = LocalDate.now();
	}
}
