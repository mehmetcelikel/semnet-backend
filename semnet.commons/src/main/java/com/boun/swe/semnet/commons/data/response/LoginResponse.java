package com.boun.swe.semnet.commons.data.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse extends ActionResponse{

	private String token;
	private String id;
	
}