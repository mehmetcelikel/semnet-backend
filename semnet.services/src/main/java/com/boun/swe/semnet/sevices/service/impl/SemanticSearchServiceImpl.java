package com.boun.swe.semnet.sevices.service.impl;

import org.springframework.stereotype.Service;

import com.boun.swe.semnet.commons.data.request.BasicSearchRequest;
import com.boun.swe.semnet.commons.data.response.QueryLabelResponse;
import com.boun.swe.semnet.commons.dbpedia.SPARQLRunner;
import com.boun.swe.semnet.sevices.service.BaseService;
import com.boun.swe.semnet.sevices.service.SemanticSearchService;


@Service
public class SemanticSearchServiceImpl extends BaseService implements SemanticSearchService{

	@Override
	public QueryLabelResponse queryLabel(BasicSearchRequest request){
		
		validate(request);
		
		return SPARQLRunner.getInstance().runQuery(request.getQueryString());
	}
}
