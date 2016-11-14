package com.boun.swe.semnet.commons.data.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BasicQueryRequest extends BaseRequest{

	@NotNull
	private String id;
}
