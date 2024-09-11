package com.dlerin.application.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin_busines_category")
public class AdminBusinessCategory {

	@Id
	@Column(name = "business_category_id")
	private String businessCategoryId;

	@Column(name = "business_category_name")
	private String businessCategoryName;

	@Column(name = "updated_by")
	private String updatedBy;

	@UpdateTimestamp
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	@Transient
	private String empId;
	
	@PrePersist
	private void prePersist() {
		this.businessCategoryId = updatedBy + "_" + businessCategoryName;
	}

}
