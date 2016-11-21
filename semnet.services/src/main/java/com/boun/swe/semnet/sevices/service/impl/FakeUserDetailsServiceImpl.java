package com.boun.swe.semnet.sevices.service.impl;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.boun.swe.semnet.sevices.service.FakeUserDetailsService;

@Service
public class FakeUserDetailsServiceImpl implements FakeUserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new User(username, "password", getGrantedAuthorities(username));
	}

	private Collection<? extends GrantedAuthority> getGrantedAuthorities(String username) {
		Collection<? extends GrantedAuthority> authorities;
		authorities = Arrays.asList(() -> "ROLE_BASIC");
		return authorities;
	}
}
