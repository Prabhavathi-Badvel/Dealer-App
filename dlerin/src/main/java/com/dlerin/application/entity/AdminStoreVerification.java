package com.dlerin.application.entity;

import java.time.LocalDate;
import java.util.Random;

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
@Table(name = "admin_store_verification")
public class AdminStoreVerification {

	@Id
	@Column(name = "dler_status_updated_by")
	private String adminStatusUpdatedBy;

	@Column(name = "store id")
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

	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	private LocalDate createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	private LocalDate updatedDate;

	@PrePersist
	private void prePersist() {
		this.adminStatusUpdatedBy = updatedBy + "_" + verificationStatus;
	}

}
