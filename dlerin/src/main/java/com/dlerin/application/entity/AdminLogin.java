package com.dlerin.application.entity;

import java.util.Collection;
import java.util.Collections;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
public class AdminLogin implements UserDetails {

	@Id
	@Column(name = "emp_id")
	private String empId;
	@Column(name = "email_id")
	private String emailId;
	@Column(name = "mobile_no")
	private String mobileNo;
	@Column(name = "name")
	private String name;
	@Column(name = "address")
	private String address;

	@Column(name = "role")
	private String userType;

	@Column(name = "password")
	private String password;

	@CreationTimestamp
	@Column(name = "reg_date")
	private String registeredDate;

	@Column(name = "update_by")
	private String updatedBy;

	@PrePersist
	private void prePersist() {
		this.updatedBy = empId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + getUserType()));
	}

	@Override
	public String getUsername() {
		return this.emailId;
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
