package com.boun.swe.semnet.commons.exception;

import com.boun.swe.semnet.commons.data.response.ErrorResponse;
import com.boun.swe.semnet.commons.type.ErrorCode;

public class SemNetRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 2423713819038648809L;
	
	private int status;
    private String errorCode;
    private String errorMessage;
    private String developerMessage;

    public SemNetRuntimeException(int httpStatus, ErrorCode errorCode, String additionalInfo, String developerMessage) {
        this.status = httpStatus;
        this.errorCode = errorCode.getCode();
        
        if(additionalInfo != null){
        	this.errorMessage = errorCode.format(additionalInfo);
        }else{
        	this.errorMessage = errorCode.getMessage();	
        }
        
        this.developerMessage = developerMessage;
    }
    
    public SemNetRuntimeException(int httpStatus, ErrorCode errorCode, String developerMessage) {
        this.status = httpStatus;
        this.errorCode = errorCode.getCode();
    	this.errorMessage = errorCode.getMessage();	
        this.developerMessage = developerMessage;
    }

//    public Response getResponse() {
//        return new Response(status, getErrorResponse());
//    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    public ErrorResponse getErrorResponse() {
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(errorCode);
        response.setApplicationMessage(developerMessage);
        response.setConsumerMessage(errorMessage);
        return response;
    }
}
