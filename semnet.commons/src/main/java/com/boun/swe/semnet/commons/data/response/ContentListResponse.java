package com.boun.swe.semnet.commons.data.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.boun.swe.semnet.commons.type.ErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentListResponse extends ActionResponse {

	private List<ContentObj> contentList;
	
	@JsonIgnore
	private boolean sortByRank;
	
	@JsonIgnore
	private boolean sortByLikeCount;
	
	public ContentListResponse(ErrorCode code){
		super(code);
	}
	
	public ContentListResponse(ErrorCode code, List<ValidationError> errors) {
		super(code);
		setErrors(errors);
	}
	
	public void addContent(String id, String description, String ownerId, Date creationDate, String ownerUsername, boolean hasImage, int likeCount){
		addContent(new ContentObj(id, description, creationDate, ownerId, ownerUsername, hasImage, likeCount));
	}
	
	public void addContent(ContentObj content){
		if(contentList == null){
			contentList = new ArrayList<ContentObj>();
		}
		
		if(!contentList.contains(content)){
			contentList.add(content);			
		}
	}
	
	public List<ContentObj> getContentList(){
		if(contentList == null || contentList.isEmpty()){
			return contentList;
		}
		
		if(sortByRank){
			Collections.sort(contentList, new ContentSort());
		}else if(sortByLikeCount){
			Collections.sort(contentList, new ContentLikeCountSort());
		}else{
			Collections.sort(contentList, new ContentSortByDate());
		}
		
		return contentList;
	}
	
	private static class ContentSort implements Comparator<ContentObj> {

	    @Override
	    public int compare(ContentObj o1, ContentObj o2) {
	    	return (o1.getRank() >= o2.getRank()) ? -1 : 1;
	    }
	}
	
	private static class ContentLikeCountSort implements Comparator<ContentObj> {

	    @Override
	    public int compare(ContentObj o1, ContentObj o2) {
	    	return (o1.getLikeCount() >= o2.getLikeCount()) ? -1 : 1;
	    }
	}
	
	private static class ContentSortByDate implements Comparator<ContentObj> {

	    @Override
	    public int compare(ContentObj o1, ContentObj o2) {
	    	return (o1.getCreationDate().compareTo(o2.getCreationDate()) > 0) ? -1 : 1;
	    }
	}
}
