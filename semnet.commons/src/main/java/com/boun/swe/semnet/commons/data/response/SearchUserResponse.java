package com.boun.swe.semnet.commons.data.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchUserResponse extends ActionResponse {

	private List<UserObj> userList;
	
	public void addUser(String id, String username, String firstname, String lastname){
		if(userList == null){
			userList = new ArrayList<UserObj>();
		}
		userList.add(new UserObj(id, username, firstname, lastname));
	}
	
	@Data
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class UserObj{
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
}
