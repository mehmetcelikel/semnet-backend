package com.boun.swe.semnet.commons.data.request;

import javax.validation.constraints.NotNull;

import com.boun.swe.semnet.commons.data.TagData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TagSearchRequest extends BaseRequest{

	@NotNull
	private TagData tagData;
}
