package com.dlerin.application.entity;

import org.hibernate.annotations.CreationTimestamp;

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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin_login")
public class AdminLogin {

	@Id
	@Column(name = "emp_id")
	private String empId;
	@Column(name = "email_id")
	private String emailId;
	@Column(name = "mobile_no")
	private long mobileNo;
	@Column(name = "name")
	private String name;
	@Column(name = "address")
	private String address;
	@Column(name = "role")
	private String role;
	@Column(name = "password")
	private String password;

	@CreationTimestamp
	@Column(name = "reg_date")
	private String registeredDate;
	@Column(name = "update_by")
	private String updatedBy;

}
