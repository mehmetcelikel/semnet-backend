package com.boun.swe.semnet.commons.exception;

import com.boun.swe.semnet.commons.data.response.ErrorResponse;
import com.boun.swe.semnet.commons.util.SemNetErrorBundle;

public class SemNetException extends RuntimeException {

    private String key;

    public SemNetException(String key){
        this.key = key;
    }

    public ErrorResponse getErrorResponse() {
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(SemNetErrorBundle.getErrorCode(key));
        response.setApplicationMessage(SemNetErrorBundle.getApplicationErrorMessage(key));
        response.setConsumerMessage(SemNetErrorBundle.getConsumerErrorMessage(key));
        return response;
    }
}
