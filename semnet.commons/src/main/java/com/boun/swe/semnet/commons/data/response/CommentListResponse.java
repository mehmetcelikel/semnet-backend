package com.boun.swe.semnet.commons.data.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.boun.swe.semnet.commons.type.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentListResponse extends ActionResponse {

	private List<CommentObj> commentList;
	
	public CommentListResponse(ErrorCode code){
		super(code);
	}
	
	public CommentListResponse(ErrorCode code, List<ValidationError> errors) {
		super(code);
		setErrors(errors);
	}
	
	public void addComment(String id, String description, String ownerId, Date creationDate, String ownerUsername){
		if(commentList == null){
			commentList = new ArrayList<CommentObj>();
		}
		commentList.add(new CommentObj(id, description, creationDate, ownerId, ownerUsername));
	}
}
