package com.boun.swe.semnet.commons.data.request;

import javax.validation.constraints.NotNull;

import com.boun.swe.semnet.commons.type.ContentListType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ListContentRequest extends BaseRequest{

	@NotNull
	private ContentListType type;
	
	@NotNull
	private String userId;
	
	private double latitude;
	private double longitude;
}
