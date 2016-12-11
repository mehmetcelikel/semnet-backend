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
public class Friendship extends BaseEntity{

	private static final long serialVersionUID = -1629115676756775849L;

	private User user;
	
	private Date creationTime;
	private boolean active;
}
