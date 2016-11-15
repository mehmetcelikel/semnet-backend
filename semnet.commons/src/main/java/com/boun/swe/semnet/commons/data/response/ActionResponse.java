package com.boun.swe.semnet.commons.data.response;

import java.util.List;

import com.boun.swe.semnet.commons.type.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionResponse {

    private String errorCode;
    private String message;
    private List<ValidationError> errors;
    
    public ActionResponse(ErrorCode code){
        this.errorCode = code.getFormattedCode();
        this.message = code.getMessage();
    }
    
    public ActionResponse(ErrorCode code, List<ValidationError> errors){
    	this(code);
    	this.errors = errors;
    }
}
