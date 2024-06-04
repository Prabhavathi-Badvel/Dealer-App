package com.dlerin.application.entity;

import java.util.Date;

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
@Table(name="dler_material_avaiabiity")
public class DlerMaterialAvailability {
	
	@Id
	@Column(name = "dler_id_material_id")
	private String dlerIdMaterialId;
	@Column(name = "dler_id")
	private String dlerId;
	@Column(name="availability")
	private String availability;
	@Column(name="return_policy")
	private String returnPolicy;
	@Column(name="updated_by")
	private String updatedBy;
	@Column(name="updated_date")
	private Date updatedDate;
	@Column(name="online_display")
	private String onlineDisplay;

	
}
