package com.boun.swe.semnet.sevices.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.swe.semnet.commons.data.request.BasicSearchRequest;
import com.boun.swe.semnet.commons.data.response.QueryLabelResponse;
import com.boun.swe.semnet.commons.dbpedia.SPARQLRunner;
import com.boun.swe.semnet.sevices.db.manager.SemanticTagManager;
import com.boun.swe.semnet.sevices.service.BaseService;
import com.boun.swe.semnet.sevices.service.SemanticSearchService;


@Service
public class SemanticSearchServiceImpl extends BaseService implements SemanticSearchService{

	@Autowired
	SemanticTagManager semanticTagManager;
	
	@Override
	public QueryLabelResponse queryLabel(BasicSearchRequest request){
		
		validate(request);
		
		return SPARQLRunner.getInstance().runQuery(request.getQueryString());
	}
}
