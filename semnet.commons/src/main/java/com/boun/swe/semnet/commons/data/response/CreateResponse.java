package com.boun.swe.semnet.commons.data.response;

import java.util.List;

import com.boun.swe.semnet.commons.type.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateResponse extends ActionResponse {

	private String id;
	private String token;
	
	public CreateResponse(ErrorCode code) {
		super(code);
	}
	
	public CreateResponse(ErrorCode code, List<ValidationError> errors) {
		super(code);
		setErrors(errors);
	}
	
	public CreateResponse(ErrorCode code, String id){
		super(code);
		
		this.id = id;
	}
	
	public CreateResponse(ErrorCode code, String id, String token){
		this(code, id);
		
		this.token = token;
	}
}