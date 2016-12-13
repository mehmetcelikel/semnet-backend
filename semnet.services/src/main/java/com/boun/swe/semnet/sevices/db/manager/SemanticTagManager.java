package com.boun.swe.semnet.sevices.db.manager;

import com.boun.swe.semnet.commons.data.response.QueryLabelResponse;

public interface SemanticTagManager {

	public QueryLabelResponse queryLabel(String input);
}
