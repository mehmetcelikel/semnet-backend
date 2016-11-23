package com.boun.swe.semnet.sevices.db.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.boun.swe.semnet.sevices.db.manager.UserManager;
import com.boun.swe.semnet.sevices.db.model.User;
import com.boun.swe.semnet.sevices.db.repo.UserRepository;

@Service
public class UserManagerImpl implements UserManager{

	@Autowired
	private UserRepository userRepository;
	
    @Cacheable(value="userCache", key="#username")
	@Override
	public User findByUsernameAndPassword(String username, String password) {
		return userRepository.findByUsernameAndPassword(username, password);
	}

    @Cacheable(value="userCache", key="#username")
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Cacheable(value="userCache", key="#id")
	@Override
	public User findById(String id) {
		return userRepository.findById(id);
	}

	@Caching(
	        put = {
	                @CachePut(value = "userCache", key = "#user.id"),
	                @CachePut(value = "userCache", key = "#user.username")
	        }
	)
	@Override
	public User merge(User user) {
		return userRepository.merge(user);
	}
	
	@Override
	public User findByOneTimeToken(String oneTimeToken) {
		return userRepository.findByOneTimeToken(oneTimeToken);
	}

	@Override
	public List<User> searchUser(String queryString) {
		return userRepository.searchUser(queryString);
	}
}