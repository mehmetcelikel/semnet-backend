package com.boun.swe.semnet.commons.data.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AddContentRequest extends BaseRequest{
	
	private String description;
	private boolean hasImage;
}
