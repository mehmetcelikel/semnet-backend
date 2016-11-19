package com.boun.swe.semnet.sevices.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.boun.swe.semnet.commons.util.KeyUtils;
import com.boun.swe.semnet.sevices.db.model.User;
import com.boun.swe.semnet.sevices.service.LoginService;
import com.boun.swe.semnet.sevices.session.SemNetSession;

@Service
public class LoginServiceImpl implements LoginService{

	@Cacheable(value="userCache", key="#user.id")
	@Override
	public User login(User user) {
		
		System.out.println("Running login query");
		
		String token = KeyUtils.currentTimeUUID().toString();
		user.setToken(token);
		
		SemNetSession.getInstance().addToken(token, user);
		
		return user;
	}

	@Caching(
	        evict = {
	        		@CacheEvict(value="loginCache", key="#user.id")
	        }
	)
	@Override
	public void logout(User user) {
		SemNetSession.getInstance().removeToken(user.getToken());
	}
}
