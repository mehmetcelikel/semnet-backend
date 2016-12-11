package com.boun.swe.semnet.sevices.db.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment extends BaseEntity{

	private static final long serialVersionUID = 888169976837026962L;

	private String description;
	
	private String ownerId;
	
	private Date creationDate;
}