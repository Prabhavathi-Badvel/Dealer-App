package com.dlerin.application.entity;

import java.util.Random;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dler_business_login")
@Builder
public class DlerBusinessLogin {

	@Id
	@Column(name = "dler_user_id")
	private String dlerUserId;
	@Column(name = "dler_email_id")
	private String dlerEmailId;
	@Column(name = "dler_mobile_no")
	private long dlerMobileNo;
	@Column(name = "dler_name")
	private String dlerName;
	@Column(name = "dler_password")
	private String dlerPassword;

	@CreationTimestamp
	@Column(name = "dler_reg_date")
	private String dlerRegDate;

	@Column(name = "dler_email_otp")
	private String dlerEmailOtp;
	@Column(name = "dler_mobile_otp")
	private String dlerMobileOtp;
	@Column(name = "dler_email_verify")
	private String dlerEmailVerify;
	@Column(name = "dler_mobile_verify")
	private String dlerMobileVerify;

	@Column(name = "dler_status")
	@Builder.Default
	private String dlerStatus = "active";
	@Column(name = "dler_status_updated_by")
	private String dlerStatusUpdatedBy;
	@UpdateTimestamp
	@Column(name = "dler_password_updated_date")
	private String dlerPasswordUpdatedDate;

	@PrePersist
	private void prePersist() {
		long sequenceNumber = generateRandomSixDigitNumber();
		this.dlerUserId = "DL" + sequenceNumber;
	}

	private static final Random random = new Random();

	private long generateRandomSixDigitNumber() {

		long randomLong = random.nextLong(9000000L) + 1000000L;
		return randomLong;
	}
}
