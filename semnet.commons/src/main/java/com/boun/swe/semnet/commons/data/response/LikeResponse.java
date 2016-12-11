package com.boun.swe.semnet.commons.data.response;

import java.util.List;

import com.boun.swe.semnet.commons.type.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeResponse extends ActionResponse {

	private int likeCount;

	public LikeResponse(ErrorCode code, int likeCount){
		super(code);
		this.likeCount = likeCount;
	}
	
	public LikeResponse(ErrorCode code, List<ValidationError> errors) {
		super(code);
		setErrors(errors);
	}
}
