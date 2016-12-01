package com.boun.swe.semnet.sevices.db.manager;

public interface ImagePersistencyManager {

	public void saveImage(String id, byte[] image);
	public byte[] getImage(String id);
	
	public void saveProfileImage(String id, byte[] image);
	public byte[] getProfileImage(String id);
}