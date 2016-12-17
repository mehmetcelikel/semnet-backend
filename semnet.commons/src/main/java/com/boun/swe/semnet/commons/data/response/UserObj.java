package com.boun.swe.semnet.commons.data.response;

import java.util.List;

import com.boun.swe.semnet.commons.data.TagData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserObj{
	
	private String id;
	private String username;
	private String firstname;
	private String lastname;
	
	private List<TagData> tagList;
	private float rank;
	
	public UserObj(String id, String username, String firstname, String lastname, List<TagData> tagList){
		this.id = id;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.tagList = tagList;
	}
	
	public UserObj(String id, String username, String firstname, String lastname, List<TagData> tagList, float rank){
		this(id, username, firstname, lastname, tagList);
		this.rank = rank;
	}
}
