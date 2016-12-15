package com.boun.swe.semnet.commons.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagData {

	private String tag;
	private String clazz;
	
	public TagData(){
	}
	
	public TagData(String tag, String clazz){
		this.tag = tag;
		this.clazz = clazz;
	}
	
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		
		if(getClazz() == null){
			return false;
		}
		
		TagData d = (TagData)o;
		
		if(d.getClazz() == null){
			return false;
		}
		
		return d.getTag().equalsIgnoreCase(getTag()) && d.getClazz().equalsIgnoreCase(getClazz());
	}
	
	@Override
	public int hashCode() {
		int code = 7;
		code = 13 * code * getTag().hashCode() * getTag().hashCode();
		return code;
	}
}
