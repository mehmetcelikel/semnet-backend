package com.boun.swe.semnet.commons.data.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.boun.swe.semnet.commons.type.ErrorCode;
import com.boun.swe.semnet.commons.util.SemNetUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryLabelResponse extends ActionResponse{

	@JsonIgnore
	private String queryString;
	
	private List<DataObj> dataList = new ArrayList<>();
	
	public QueryLabelResponse(ErrorCode code, List<ValidationError> errors) {
		super(code);
		setErrors(errors);
	}
	
	public QueryLabelResponse(String queryString){
		super(ErrorCode.SUCCESS);
		this.queryString = queryString;
	}
	
	public void addData(String label, String clazz, String description){
		if(dataList == null){
			dataList = new ArrayList<DataObj>();
		}
		dataList.add(new DataObj(label, clazz, description));
	}
	
	public List<DataObj> getDataList(){
		Collections.sort(dataList, new DataObjSort(queryString));
		return dataList;		
	}
	
	@Data
	private static class DataObj{
		private String label;
		private String clazz;
		private String description;
		
		private DataObj(String label, String clazz, String description){
			this.label = label;
			this.clazz = clazz;
//			this.description = description; 
		}
	}
	
	private static class DataObjSort implements Comparator<DataObj> {

		private String queryString;
		
		public DataObjSort(String queryString){
			this.queryString = queryString;
		}
		
	    @Override
	    public int compare(DataObj o1, DataObj o2) {
	    	if(o1 == null || o2 == null){
	    		return 1;
	    	}
	    	float idx1 = SemNetUtils.getSimilarityIndex(o1.getLabel(), queryString);
	    	float idx2 = SemNetUtils.getSimilarityIndex(o2.getLabel(), queryString);
	    	
	    	return (idx1 >= idx2) ? -1 : 1;
	    }
	}
}
