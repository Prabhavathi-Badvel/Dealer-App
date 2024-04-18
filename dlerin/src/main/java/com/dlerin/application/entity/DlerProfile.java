package com.dlerin.application.entity;

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
@Table(name="dler_profile")
public class DlerProfile {

	@Id
	@Column(name="dler_business_id")
	private String dlerBusinessId;
	@Column(name="dler_id")
	private String dlerId;
	@Column(name="dler_business_name")
	private String dlerBusinessName;
	@Column(name="dler_business_location")
	private String dlerBusinessLocation;
	@Column(name="dler_business_contact_person")
	private String dlerBusinessContactPerson;
	@Column(name="dler_business_contact_no")
	private String dlerBusinessContactNo;
	
	

	
}
