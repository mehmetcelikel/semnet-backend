package com.boun.swe.semnet.sevices.db.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.boun.swe.semnet.commons.data.TagData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaggedEntity extends BaseEntity{

	private static final long serialVersionUID = 1989491785120176305L;

	private List<TagData> tagList;
	
    @Transient
    private EntityType entityType;
    
	public TaggedEntity(EntityType type) {
		this.entityType = type;
	}

    public enum EntityType{
    	USER, CONTENT;
    }
    
	public boolean updateTagList(TagData tag, boolean add){
		if(add){
			return add(tag);
		}
		
		return remove(tag);
	}
	
	public List<TagData> removeTagList(List<TagData> newList){
		
		if(tagList == null || tagList.isEmpty()){
			return null;
		}
		
		if(newList == null || newList.isEmpty()){
			List<TagData> copyTagList = new ArrayList<TagData>(tagList);
			tagList = null;
			return copyTagList;
		}
		
		List<TagData> removeList = new ArrayList<TagData>();
		
		List<TagData> copyTagList = new ArrayList<TagData>(tagList);
		for (TagData tag : copyTagList) {
			if(!newList.contains(tag)){
				removeList.add(tag);
			}
		}
		tagList = newList;
		
		return removeList;
	}
	
	public List<TagData> addTagList(List<TagData> newList){
		
		if(newList == null || newList.isEmpty()){
			return null;
		}
		
		if(tagList == null || tagList.isEmpty()){
			tagList = newList;
			return tagList;
		}

		List<TagData> addedList = new ArrayList<TagData>();
		
		for (TagData newTag : newList) {
			if(!tagList.contains(newTag)){
				add(newTag);
				addedList.add(newTag);
			}
		}
		return addedList;
	}
	
	private boolean remove(TagData tag){
		
		if(tag == null || "".equalsIgnoreCase(tag.getTag())){
			return false;
		}
		
		if(tagList == null || tagList.isEmpty()){
			return false;
		}
		
		return tagList.remove(tag);
	}
	
	private boolean add(TagData tag){
		
		if(tag == null || "".equalsIgnoreCase(tag.getTag())){
			return false;
		}
		
		if(tagList == null || tagList.isEmpty()){
			tagList = new ArrayList<TagData>();
		}
		
		if(!tagList.contains(tag)){
			tagList.add(tag);
			return true;
		}
		
		return false;
	}
}
