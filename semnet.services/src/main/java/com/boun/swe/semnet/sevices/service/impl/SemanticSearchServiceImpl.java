package com.boun.swe.semnet.sevices.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.boun.swe.semnet.commons.data.request.BasicSearchRequest;
import com.boun.swe.semnet.commons.data.response.QueryLabelResponse;
import com.boun.swe.semnet.commons.dbpedia.SPARQLRunner;
import com.boun.swe.semnet.sevices.service.BaseService;
import com.boun.swe.semnet.sevices.service.SemanticSearchService;


@Service
public class SemanticSearchServiceImpl extends BaseService implements SemanticSearchService{

	private final static Logger logger = LoggerFactory.getLogger(SemanticSearchServiceImpl.class);
	
	@Override
	public QueryLabelResponse queryLabel(BasicSearchRequest request){
		
		logger.info("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR1");
		
		validate(request);
		
		logger.info("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR2");
		
		return SPARQLRunner.getInstance().runQuery(request.getQueryString());
	}
}
