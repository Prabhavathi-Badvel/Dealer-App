package com.dlerin.application.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
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
@Table(name = "Admin_plan_membership")
public class PlanMembership {

	@Id
	@Column(name = "plan_id")
	private String planId;
	
	@Column(name = "plan_name")
	private String planName;
	
	@Column(name = "amount")
	private double amount;
	
	@Column(name = "number_of_days")
	private long numberOfDays;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@UpdateTimestamp
	@Column(name="updated_date")
	private LocalDate updatedDate;
}
