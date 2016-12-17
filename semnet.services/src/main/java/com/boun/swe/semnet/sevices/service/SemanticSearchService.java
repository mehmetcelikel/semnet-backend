package com.boun.swe.semnet.sevices.service;

import com.boun.swe.semnet.commons.data.request.BasicSearchRequest;
import com.boun.swe.semnet.commons.data.request.TagSearchRequest;
import com.boun.swe.semnet.commons.data.response.ContentListResponse;
import com.boun.swe.semnet.commons.data.response.QueryLabelResponse;
import com.boun.swe.semnet.commons.data.response.UserListResponse;

public interface SemanticSearchService {

	public QueryLabelResponse queryLabel(BasicSearchRequest request);
	
	public QueryLabelResponse querySearchString(BasicSearchRequest request);
	
	public ContentListResponse searchContent(TagSearchRequest request);
	public UserListResponse searchUser(TagSearchRequest request);
}
