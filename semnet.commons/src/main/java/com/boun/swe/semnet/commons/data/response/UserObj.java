package com.boun.swe.semnet.commons.data.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserObj{
	
	private String id;
	private String username;
	private String firstname;
	private String lastname;
	
	public UserObj(String id, String username, String firstname, String lastname){
		this.id = id;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
}
