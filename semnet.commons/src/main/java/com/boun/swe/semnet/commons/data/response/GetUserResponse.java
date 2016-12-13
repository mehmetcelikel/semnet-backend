package com.boun.swe.semnet.commons.data.response;

import java.util.List;

import com.boun.swe.semnet.commons.type.ErrorCode;
import com.boun.swe.semnet.commons.type.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetUserResponse extends ActionResponse {

	private String username;
	private String firstname;
	private String lastname;
	private String email;
	private String phone;
	
	private UserStatus status;

	public GetUserResponse(ErrorCode code){
		super(code);
	}
	
	public GetUserResponse(ErrorCode code, List<ValidationError> errors) {
		super(code);
		setErrors(errors);
	}
}
