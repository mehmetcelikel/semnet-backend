package com.boun.swe.semnet.sevices.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.boun.swe.semnet.commons.constants.AppConstants;
import com.boun.swe.semnet.commons.data.response.ActionResponse;
import com.boun.swe.semnet.commons.data.response.ValidationError;
import com.boun.swe.semnet.commons.type.ErrorCode;
import com.boun.swe.semnet.sevices.db.model.TaggedEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SemanticSearchResponse extends ActionResponse{
	
	private List<SearchDetail> resultList;
	
	public SemanticSearchResponse(){
		super(ErrorCode.SUCCESS);
	}
	
	public SemanticSearchResponse(ErrorCode code, List<ValidationError> errors) {
		super(code);
		setErrors(errors);
	}
	
	public void addDetail(TaggedEntity.EntityType type, String id, float rank, boolean increase){
		if(resultList == null){
			resultList = new ArrayList<SearchDetail>();
		}
		
		SearchDetail detail = new SearchDetail(type, id, rank);
		
		if(!resultList.contains(detail)){
			resultList.add(detail);
			return;
		}
		
		resultList.remove(detail);
		
		if(increase){
			detail.setRank(rank + AppConstants.SEMANTIC_CO_OCCURANCE_FACTOR);	
		}
		
		resultList.add(detail);	
	}
	
	public List<SearchDetail> getResultList(){
		Collections.sort(resultList, new SearchDetailSort());
		return resultList;
	}
	
	@Data
	private static class SearchDetail{
		private TaggedEntity.EntityType type;
		private String id;
		
		private float rank;
		
		public SearchDetail(TaggedEntity.EntityType type, String id, float rank){
			this.type = type;
			this.id = id;
			this.rank = rank;
		}
		
		public boolean equals(Object o){
			if(o == null){
				return false;
			}
			SearchDetail idx = (SearchDetail)o;
			
			return (idx.getId().equalsIgnoreCase(this.getId())) && (idx.getType() == this.getType());
		}
		
		@Override
		public int hashCode() {
			int code = 7;
			code = 89 * code * this.getId().hashCode();
			code = code * this.getType().hashCode();
			return code;
		}
	}
	
	private static class SearchDetailSort implements Comparator<SearchDetail> {

	    @Override
	    public int compare(SearchDetail o1, SearchDetail o2) {
	    	
	    	return (o1.getRank() >= o2.getRank()) ? -1 : 1;
	    }
	}
}
