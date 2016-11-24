package com.boun.swe.semnet.sevices.db.manager.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.boun.swe.semnet.commons.chronicle.PersistedMap;
import com.boun.swe.semnet.sevices.db.manager.ImagePersistencyManager;

@Scope("singleton")
@Service
public class ImagePersistencyManagerImpl implements ImagePersistencyManager{

	private PersistedMap persistedMap = new PersistedMap("ImageMap");
	
	public void saveImage(String id, byte[] image){
		persistedMap.put(id, image);
	}
	
	public byte[] getImage(String id){
		return persistedMap.get(id);
	}
}