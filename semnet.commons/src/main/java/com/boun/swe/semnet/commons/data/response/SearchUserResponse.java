package com.boun.swe.semnet.commons.data.response;

import java.util.ArrayList;
import java.util.List;

import com.boun.swe.semnet.commons.type.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchUserResponse extends ActionResponse {

	private List<UserObj> userList;
	
	public SearchUserResponse(ErrorCode code){
		super(code);
	}
	
	public void addUser(String id, String username, String firstname, String lastname){
		if(userList == null){
			userList = new ArrayList<UserObj>();
		}
		userList.add(new UserObj(id, username, firstname, lastname));
	}
}
