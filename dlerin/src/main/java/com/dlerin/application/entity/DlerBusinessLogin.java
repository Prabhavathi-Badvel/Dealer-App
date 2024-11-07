package com.dlerin.application.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dler_business_login")
public class DlerBusinessLogin implements UserDetails {

	@Id
	@Column(name = "dler_user_id")
	private String dlerUserId;
	@Column(name = "dler_email_id")
	private String dlerEmailId;
	@Column(name = "dler_mobile_no")
	private String dlerMobileNo;
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
	private String dlerStatus;
	@Column(name = "dler_status_updated_by")
	private String dlerStatusUpdatedBy;
	@UpdateTimestamp
	@Column(name = "dler_password_updated_date")
	private String dlerPasswordUpdatedDate;

    @Column(name = "user_type")
	private String userType;

	@PrePersist
	private void prePersist() {
		long sequenceNumber = generateRandomSixDigitNumber();
		this.dlerUserId = "DL" + sequenceNumber;
		this.dlerStatusUpdatedBy = dlerUserId;
	}

	private static final Random random = new Random();

	private long generateRandomSixDigitNumber() {

		long randomLong = random.nextLong(9000000L) + 1000000L;
		return randomLong;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + getUserType()));
	}

	@JsonIgnore
	@Override
	public String getPassword() {

		return this.dlerPassword;
	}

	@Override
	public String getUsername() {
		return this.dlerEmailId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
