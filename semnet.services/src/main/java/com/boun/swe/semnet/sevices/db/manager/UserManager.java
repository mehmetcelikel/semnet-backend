package com.boun.swe.semnet.sevices.db.manager;

import java.util.List;

import com.boun.swe.semnet.sevices.db.model.User;

public interface UserManager {

	public User findByUsername(String username);
		
	public User findByOneTimeToken(String oneTimeToken);

	public List<User> searchUser(String queryString);
		
	public User findById(String id);
		
	public User merge(User user);
	public User updateCache(User user);
	
	User login(String authToken, User user);
	void logout(String authToken);
	
	public List<User> getAllUsers();
}
