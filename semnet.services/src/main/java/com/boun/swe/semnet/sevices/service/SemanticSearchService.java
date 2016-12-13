package com.boun.swe.semnet.sevices.service;

import com.boun.swe.semnet.commons.data.request.BasicSearchRequest;
import com.boun.swe.semnet.commons.data.response.QueryLabelResponse;

public interface SemanticSearchService {

	public QueryLabelResponse queryLabel(BasicSearchRequest request);
}
