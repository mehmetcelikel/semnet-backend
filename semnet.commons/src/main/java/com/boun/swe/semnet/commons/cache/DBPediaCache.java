package com.boun.swe.semnet.commons.cache;

import java.util.Hashtable;

import com.boun.swe.semnet.commons.data.response.QueryLabelResponse;

public class DBPediaCache {

	private static DBPediaCache instance = new DBPediaCache();
	
	private Hashtable<String, QueryLabelResponse> cacheTable = new Hashtable<String, QueryLabelResponse>();
	
	private DBPediaCache() {
	}
	
	public static DBPediaCache getInstance() {
		return instance;
	}
	
	public QueryLabelResponse get(String queryString){
		return cacheTable.get(queryString);
	}
	
	public void put(String queryString, QueryLabelResponse resp){
		cacheTable.put(queryString, resp);
	}
}
