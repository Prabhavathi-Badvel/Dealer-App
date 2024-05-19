package com.dlerin.application.securities;

import java.io.IOException;

import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private  JwtService jwtService;

	@Autowired
	private  UserDetailsService userDetailsService;




	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String token = extractToken(request);
		if (token != null && jwtService.isTokenValid(token)) {
			String username = jwtService.extractUsername(token);
			String userType = jwtService.extractUserType(token);
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}

	private String extractToken(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}




//	@Override
//	protected void doFilterInternal(HttpServletRequest request,
//									HttpServletResponse response,
//									FilterChain filterChain) throws ServletException, IOException {
//		final String authHeader = request.getHeader("Authorization");
//
//		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//			filterChain.doFilter(request, response);
//			return;
//		}
//
//		String token = authHeader.substring(7);
//		String username = jwtService.extractUsername(token);
//
//		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//		if (!jwtService., userDetails)) {
//			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
//			return;
//		}
//		// If the token is valid, set up Spring Security context
//		UsernamePasswordAuthenticationToken authentication =
//				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//
//		// Continue with the filter chain
//		filterChain.doFilter(request, response);
//	}


}


