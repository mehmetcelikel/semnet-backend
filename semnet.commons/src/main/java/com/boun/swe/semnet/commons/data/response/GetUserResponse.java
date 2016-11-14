package com.boun.swe.semnet.commons.data.response;

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
	private UserStatus status;

}
