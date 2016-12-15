package com.boun.swe.semnet.commons.data.response;

import java.util.Date;
import java.util.List;

import com.boun.swe.semnet.commons.type.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetContentResponse extends ActionResponse {

	private ContentObj content;

	public GetContentResponse(ErrorCode code){
		super(code);
	}
	
	public GetContentResponse(ErrorCode code, List<ValidationError> errors) {
		super(code);
		setErrors(errors);
	}
	
	public void setContentDetails(String id, String description, Date creationDate, String ownerId, String ownerUsername, boolean hasImage, int likeCount){
		content = new ContentObj(id, description, creationDate, ownerId, ownerUsername, hasImage, likeCount);
	}
	
	public void addToLikerList(String id, String username){
		content.addToLikerList(id, username);
	}
}
