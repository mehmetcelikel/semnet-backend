package com.boun.swe.semnet.commons.data.request;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
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

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date birthDate;
}
