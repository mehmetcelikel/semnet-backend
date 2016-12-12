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
public class ContentListResponse extends ActionResponse {

	private List<ContentObj> contentList;
	
	public ContentListResponse(ErrorCode code){
		super(code);
	}
	
	public ContentListResponse(ErrorCode code, List<ValidationError> errors) {
		super(code);
		setErrors(errors);
	}
	
	public void addContent(String id, String description, String ownerId, Date creationDate, String ownerUsername, boolean hasImage, int likeCount){
		addContent(new ContentObj(id, description, creationDate, ownerId, ownerUsername, hasImage, likeCount));
	}
	
	public void addContent(ContentObj content){
		if(contentList == null){
			contentList = new ArrayList<ContentObj>();
		}
		contentList.add(content);
	}
}
