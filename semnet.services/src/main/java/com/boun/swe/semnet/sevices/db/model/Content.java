package com.boun.swe.semnet.sevices.db.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "content")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Content extends BaseEntity{

	private static final long serialVersionUID = 6144439756047888283L;

	private String description;
	private Date creationDate;
	
	@DBRef
	private User owner;
	
	@DBRef
	private List<User> likers;
	
	private int likeCount = 0;
	
	@DBRef
	private List<Comment> comments;
}