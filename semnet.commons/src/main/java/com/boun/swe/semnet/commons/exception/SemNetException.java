package com.boun.swe.semnet.commons.exception;

import com.boun.swe.semnet.commons.type.ErrorCode;

public class SemNetException extends RuntimeException {

	private static final long serialVersionUID = -156081897455499864L;

	private ErrorCode errorCode;
	private String additionalInfo;
	
    public SemNetException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
    
    public SemNetException(ErrorCode errorCode, String additionalInfo){
        this.errorCode = errorCode;
        this.additionalInfo = additionalInfo;
    }

    public ErrorCode getErrorCode() {
		return errorCode;
	}
    
    public String getAdditionalInfo() {
		return additionalInfo;
	}
}
