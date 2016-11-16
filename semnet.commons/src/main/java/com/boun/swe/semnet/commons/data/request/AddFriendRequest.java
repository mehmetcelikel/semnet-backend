package com.boun.swe.semnet.commons.data.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AddFriendRequest extends BaseRequest{

	@NotNull
	private String friendId;
}
