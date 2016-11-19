package com.boun.swe.semnet.sevices.db.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 6068542239113172662L;

	protected BaseEntity(){
	}
	
    @Id
    private String id;

    public boolean isEqual(BaseEntity request){
    	return this.id.equalsIgnoreCase(request.getId());
    }
}
