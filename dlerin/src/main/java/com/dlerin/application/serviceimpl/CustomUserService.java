package com.dlerin.application.serviceimpl;

import java.util.Optional;

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

		Optional<DlerBusinessLogin> dler = dlerBusinessLoginRepo.findByDlerEmailIdOrDlerMobileNo(username, username);
		   if (dler.isPresent()) {
	            return dler.get();
	        }

		Optional<AdminLogin> adminLogin = adminLoginRepo.findByEmailIdOrMobileNo(username, username);
		if (adminLogin.isPresent()) {
			return adminLogin.get();
		}
		throw new UsernameNotFoundException("User not found with username: " + username);
	}

}
