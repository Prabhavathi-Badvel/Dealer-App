package com.dlerin.application.serviceimpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
	        // Load DlerBusinessLogin user
	        Optional<DlerBusinessLogin> dler = dlerBusinessLoginRepo.findByDlerEmailIdOrDlerMobileNo(username, username);
	        if (dler.isPresent()) {
	            String userType = dler.get().getUserType();
	            if (userType == null || userType.trim().isEmpty()) {
	                throw new UsernameNotFoundException("User type not found for user: " + username);
	            }

	            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userType));

	            // Return UserDetails with username, password, and authorities
	            return new org.springframework.security.core.userdetails.User(
	                    dler.get().getUsername(),
	                    dler.get().getPassword(),
	                    authorities
	            );
	        }

	        // Load AdminLogin user
	        Optional<AdminLogin> adminLogin = adminLoginRepo.findByEmailIdOrMobileNo(username, username);
	        if (adminLogin.isPresent()) {
	            String userType = adminLogin.get().getUserType();
	            if (userType == null || userType.trim().isEmpty()) {
	                throw new UsernameNotFoundException("User type not found for user: " + username);
	            }

	            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userType));

	            // Return UserDetails with username, password, and authorities
	            return new org.springframework.security.core.userdetails.User(
	                    adminLogin.get().getUsername(),
	                    adminLogin.get().getPassword(),
	                    authorities
	            );
	        }

	        // If no user is found
	        throw new UsernameNotFoundException("User not found with username: " + username);
	    }
	}