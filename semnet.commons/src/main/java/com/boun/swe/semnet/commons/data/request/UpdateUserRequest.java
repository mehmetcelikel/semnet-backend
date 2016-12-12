package com.boun.swe.semnet.commons.data.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateUserRequest extends BaseRequest{

	@NotNull
	private String id;
	private String firstname;
	private String lastname;
	private Date birthDate;

	private String email;
	private String phone;
}
