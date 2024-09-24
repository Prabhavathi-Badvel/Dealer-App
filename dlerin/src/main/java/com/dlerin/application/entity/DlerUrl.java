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
@Table(name = "dler_url")
public class DlerUrl {
	
	@Id
	@Column(name = "ui_url")
	private String uiUrl;
	
	@UpdateTimestamp
	@Column(name="updated_date")
	private LocalDate updatedDate;
	
	@Column(name="updated_by")
	private String updatedBy;
	
	@Column(name = "dler_id")
	private String dlerId;
	
	@PrePersist
	private void prePersist() {
		this.uiUrl = dlerId + "_" + uiUrl;
	}
	
}

