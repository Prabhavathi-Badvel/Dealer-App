package com.dlerin.application.entity;

import java.util.Date;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="admin_gst")
public class AdminGst {

	@Id
	@Column(name="gst_code")
	private String gstCode;
	@Column(name="gst_percentage")
	private int gstPercentage;
	@UpdateTimestamp
	@Column(name="updated_date")
	private Date updatedDate;
	@Column(name="updated_by")
	private String updatedBy;
	
	@Transient
	private String emailId;

	@Transient
	private String mobileNo;
}

