package com.boun.swe.semnet.commons.chronicle;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boun.swe.semnet.commons.constants.AppConstants;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

public final class PersistedMap {
	
	private static Logger log = LoggerFactory.getLogger(PersistedMap.class);
	
	private ChronicleMap<String, byte[]> map;

	private String name;
	
	public PersistedMap(String name) {
		this.name = name;
		this.map = createMap();
	}
	
	public String getName() {
		return name; 
	}
	
	private ChronicleMap<String, byte[]> createMap() {
		
		try {
			File folder = new File(getFolder());
			if(!folder.exists()){
				folder.mkdirs();
			}
			File file = new File(getFilename(folder.getAbsolutePath()));
			if(!file.getParentFile().exists()){
				file.mkdirs();
			}
			return ChronicleMapBuilder.of(String.class, byte[].class).averageValueSize(100 * 10).createPersistedTo(file);
		} catch (IOException e) {
			log.error("Error in createMap()", e);
			throw new RuntimeException(e);
		}
	}
	
	public void put(String key, byte[] image) {
		map.putIfAbsent(key , image);
	}
	
	public byte[] get(String key) {
		return map.get(key);
	}
	
	public void remove(String key) {
		map.remove(key);
	}
	
	public void removeAll() {
		map.clear();
	}
	
	
	private String getFolder() {
		StringBuilder buffer = new StringBuilder();
		
		String os = System.getProperty(AppConstants.OS_NAME);
		
		if(os.indexOf(AppConstants.WINDOWS) >= 0 ){
			buffer.append(AppConstants.PERSISTED_WINDOWS_PATH);	
		}else{
			buffer.append(AppConstants.PERSISTED_UNIX_PATH);
		}
		
		return buffer.toString();
	}
	
	private String getFilename(String path) {

		StringBuilder buffer = new StringBuilder();
		
		buffer.append(path);
		buffer.append( File.separator);
		buffer.append(name);
		buffer.append(AppConstants.UNDERSCORE);
		buffer.append(AppConstants.DAT);
		
		return buffer.toString();
	}

	public int getSize() {
		if ( map != null) {
			return map.size();
		}
		return 0;
	}
}
