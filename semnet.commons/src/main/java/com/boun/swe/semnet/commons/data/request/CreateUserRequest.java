package com.boun.swe.semnet.commons.data.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CreateUserRequest{

	@NotNull
	@Size(min=4)
	private String username;

	@NotNull
	private String firstname;

	@NotNull
	private String lastname;

	@NotNull
	@Size(min=4)
	private String password;

	private String birthDate;
}
