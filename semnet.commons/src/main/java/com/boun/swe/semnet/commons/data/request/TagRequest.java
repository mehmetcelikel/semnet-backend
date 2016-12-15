package com.boun.swe.semnet.commons.data.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.boun.swe.semnet.commons.data.TagData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TagRequest extends BaseRequest{

	@NotNull
	private String entityId;
	
	@NotNull
	private List<TagData> tag;
	
	private boolean add;
	
}
