package com.boun.swe.semnet.commons.data.response;

import java.util.List;

import com.boun.swe.semnet.commons.type.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateUserResponse extends ActionResponse {

	private String id;
	private String username;
	private String firstname;
	private String lastname;
	
	public CreateUserResponse(ErrorCode code) {
		super(code);
	}
	
	public CreateUserResponse(ErrorCode code, List<ValidationError> errors) {
		super(code);
		setErrors(errors);
	}
	
	public CreateUserResponse(ErrorCode code, String id, String username, String firstname, String lastname){
		super(code);
		
		this.id = id;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
}
