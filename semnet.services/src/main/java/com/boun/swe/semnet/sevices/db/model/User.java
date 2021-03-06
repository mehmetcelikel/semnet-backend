package com.boun.swe.semnet.sevices.db.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.boun.swe.semnet.commons.type.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@Document(collection = "user_")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends TaggedEntity{

	private static final long serialVersionUID = 553091158811833395L;

	@Field("username")
    private String username;

    @Field("firstname")
    private String firstname;

    @Field("lastname")
    private String lastname;

    @Field("email")
    private String email;
    
    @Field("phone")
    private String phone;
    
    @Field("password")
    private String password;

    @JsonIgnore
    @Field("oneTimeToken")
    private String oneTimeToken;

    private UserStatus status;

    @Transient
    private String token;
    
	private List<String> contents = new ArrayList<>();
    
	private List<Friendship> friendList = new ArrayList<>();
    
	public User(){
		super(EntityType.USER);
	}
	
}
