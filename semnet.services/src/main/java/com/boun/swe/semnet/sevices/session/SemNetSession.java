package com.boun.swe.semnet.sevices.session;

import java.util.Hashtable;
import java.util.Map;

import com.boun.swe.semnet.sevices.db.model.User;

public final class SemNetSession {

	private static Map<String, User> sessionStorage = new Hashtable<>();
	
	private static SemNetSession instance = new SemNetSession();
	
	private SemNetSession(){
	}
	
	public static SemNetSession getInstance() {
		return instance;
	}
	
	public boolean validateToken(String token){
		if(token == null){
			return false;
		}
		return sessionStorage.get(token) != null;
	}
	
	public void addToken(String token, User user){
		sessionStorage.put(token, user);
	}
	
	public void removeToken(String token){
		sessionStorage.remove(token);
	}
	
	public User getUser(String token){
		return sessionStorage.get(token);
	}
}
