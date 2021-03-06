package com.boun.swe.semnet.commons.data.response;

import java.util.Date;

import com.boun.swe.semnet.commons.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentObj{
	
	private String id;
	private String description;
	private String dateDiff ;
	private String ownerId;
	private String ownerUsername;
	
	public CommentObj(String id, String description, Date creationDate, String ownerId, String ownerUsername){
		this.id = id;
		this.description = description;
		this.dateDiff = DateUtils.calculateDateDifference(creationDate);
		this.ownerId = ownerId;
		this.ownerUsername = ownerUsername;
	}
	
}
