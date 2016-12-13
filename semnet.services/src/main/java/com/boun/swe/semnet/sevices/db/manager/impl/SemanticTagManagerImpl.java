package com.boun.swe.semnet.sevices.db.manager.impl;

import org.springframework.stereotype.Service;

import com.boun.swe.semnet.commons.data.response.QueryLabelResponse;
import com.boun.swe.semnet.commons.dbpedia.SPARQLRunner;
import com.boun.swe.semnet.sevices.db.manager.SemanticTagManager;

@Service
public class SemanticTagManagerImpl implements SemanticTagManager{

//	@Cacheable(value="semanticTagCache", key="#input")
	@Override
	public QueryLabelResponse queryLabel(String input){
		return SPARQLRunner.getInstance().runQuery(input);
	}
}
