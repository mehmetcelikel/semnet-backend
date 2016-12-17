package com.boun.swe.semnet.commons.data.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.boun.swe.semnet.commons.data.TagData;
import com.boun.swe.semnet.commons.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
	private Long distance;
	
	private List<TagData> tagList;
	private List<UserObj> likerList;
	
	private float rank;
	
	@JsonIgnore
	private Date creationDate;
	
	public ContentObj(String id, String description, Date creationDate, String ownerId, String ownerUsername, boolean hasImage, int likeCount){
		this.id = id;
		this.description = description;
		this.dateDiff = DateUtils.calculateDateDifference(creationDate);
		this.creationDate = creationDate;
		this.ownerId = ownerId;
		this.ownerUsername = ownerUsername;
		this.hasImage = hasImage;
		this.likeCount = likeCount;
	}
	
	public ContentObj(String id, String description, Date creationDate, String ownerId, String ownerUsername, boolean hasImage, int likeCount, float rank){
		this(id, description, creationDate, ownerId, ownerUsername, hasImage, likeCount);
		this.rank = rank;
	}
	
	public void addToLikerList(String id, String username){
		if(likerList == null){
			likerList = new ArrayList<>();
		}
		likerList.add(new UserObj(id, username, null, null, null));
	}
	
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		ContentObj idx = (ContentObj)o;
		
		return (idx.getId().equalsIgnoreCase(this.getId()));
	}
	
	@Override
	public int hashCode() {
		int code = 7;
		code = 89 * code * this.getId().hashCode();
		return code;
	}
}
