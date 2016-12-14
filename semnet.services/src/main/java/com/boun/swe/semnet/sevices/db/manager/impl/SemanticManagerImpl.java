package com.boun.swe.semnet.sevices.db.manager.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.boun.swe.semnet.commons.data.response.QueryLabelResponse;
import com.boun.swe.semnet.commons.dbpedia.SPARQLRunner;
import com.boun.swe.semnet.sevices.db.manager.SemanticManager;

@Service
public class SemanticManagerImpl implements SemanticManager{

	@Cacheable(value = "semanticTagCache", key = "#input", unless = "#result == null")
	@Override
	public QueryLabelResponse runQuery(String input){
		return SPARQLRunner.getInstance().runQuery(input);
	}
}
