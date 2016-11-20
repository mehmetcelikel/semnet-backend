package com.boun.swe.semnet.sevices.service;

import com.boun.swe.semnet.commons.data.request.AddCommentRequest;
import com.boun.swe.semnet.commons.data.request.AddContentRequest;
import com.boun.swe.semnet.commons.data.request.BasicQueryRequest;
import com.boun.swe.semnet.commons.data.request.DeleteCommentRequest;
import com.boun.swe.semnet.commons.data.request.ListContentRequest;
import com.boun.swe.semnet.commons.data.response.ActionResponse;
import com.boun.swe.semnet.commons.data.response.ContentListResponse;
import com.boun.swe.semnet.commons.data.response.GetContentResponse;

public interface ContentService {

	public ActionResponse create(AddContentRequest request);
	public GetContentResponse get(BasicQueryRequest request);
	public ContentListResponse list(ListContentRequest request);
	
	public ActionResponse like(BasicQueryRequest request);
	public ActionResponse unLike(BasicQueryRequest request);
	
	public ActionResponse addComment(AddCommentRequest request);
	public ActionResponse deleteComment(DeleteCommentRequest request);
}
