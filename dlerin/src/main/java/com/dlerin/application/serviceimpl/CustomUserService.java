package com.dlerin.application.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.AdminLogin;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.repository.AdminLoginRepo;
import com.dlerin.application.repository.DlerBusinessLoginRepo;

@Service
public class CustomUserService implements UserDetailsService {

	@Autowired
	AdminLoginRepo adminLoginRepo;

	@Autowired
	DlerBusinessLoginRepo dlerBusinessLoginRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		DlerBusinessLogin dler = dlerBusinessLoginRepo.findByDlerEmailIdOrDlerMobileNo(username, username);
		if (dler != null) {
			return dler;
		}

		AdminLogin adminLogin = adminLoginRepo.findByEmailIdOrMobileNo(username, username);
		if (adminLogin != null) {
			return adminLogin;
		}
		throw new UsernameNotFoundException("User not found with username: " + username);
	}

}
