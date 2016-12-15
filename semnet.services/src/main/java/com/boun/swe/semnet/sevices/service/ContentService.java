package com.boun.swe.semnet.sevices.service;

import com.boun.swe.semnet.commons.data.request.AddCommentRequest;
import com.boun.swe.semnet.commons.data.request.AddContentRequest;
import com.boun.swe.semnet.commons.data.request.BasicQueryRequest;
import com.boun.swe.semnet.commons.data.request.DeleteCommentRequest;
import com.boun.swe.semnet.commons.data.request.ListContentRequest;
import com.boun.swe.semnet.commons.data.request.TagRequest;
import com.boun.swe.semnet.commons.data.response.ActionResponse;
import com.boun.swe.semnet.commons.data.response.CommentListResponse;
import com.boun.swe.semnet.commons.data.response.ContentListResponse;
import com.boun.swe.semnet.commons.data.response.CreateResponse;
import com.boun.swe.semnet.commons.data.response.GetContentResponse;
import com.boun.swe.semnet.commons.data.response.LikeResponse;

public interface ContentService {

	public ActionResponse create(AddContentRequest request);
	public GetContentResponse get(BasicQueryRequest request);
	public ContentListResponse list(ListContentRequest request);
	
	public ActionResponse upload(String authToken, byte[] image, String filename, String contentId);
	public byte[] downloadContent(String authToken, String contentId);
	
	public LikeResponse like(BasicQueryRequest request);
	public LikeResponse unLike(BasicQueryRequest request);
	
	public CreateResponse addComment(AddCommentRequest request);
	public ActionResponse deleteComment(DeleteCommentRequest request);
	
	public CommentListResponse listComments(BasicQueryRequest request);
	
	public ActionResponse tag(TagRequest request);
}
