package com.boun.swe.semnet.sevices.service;

import com.boun.swe.semnet.sevices.db.model.User;

public interface LoginService {
	User login(User user);
	void logout(User user);
}
