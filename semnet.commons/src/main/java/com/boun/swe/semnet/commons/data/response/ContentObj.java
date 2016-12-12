package com.boun.swe.semnet.commons.data.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.boun.swe.semnet.commons.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentObj{
	
	private String id;
	private String description;
	private String dateDiff;
	private String ownerId;
	private String ownerUsername;
	private boolean hasImage;
	private int likeCount;
	
	private List<CommentObj> commentList;
	private List<UserObj> likerList;
	
	public ContentObj(String id, String description, Date creationDate, String ownerId, String ownerUsername, boolean hasImage, int likeCount){
		this.id = id;
		this.description = description;
		this.dateDiff = DateUtils.calculateDateDifference(creationDate);
		this.ownerId = ownerId;
		this.ownerUsername = ownerUsername;
		this.hasImage = hasImage;
		this.likeCount = likeCount;
	}
	
	public void addToLikerList(String id, String username){
		if(likerList == null){
			likerList = new ArrayList<>();
		}
		likerList.add(new UserObj(id, username, null, null));
	}
	
	public void addToCommentList(String id, String description, Date creationDate, String ownerId, String username){
		if(commentList == null){
			commentList = new ArrayList<>();
		}
		commentList.add(new CommentObj(id, description, creationDate, ownerId, username));
	}
}
