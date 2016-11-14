package com.boun.swe.semnet.sevices.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.boun.swe.semnet.commons.data.request.BaseRequest;
import com.boun.swe.semnet.commons.exception.SemNetRuntimeException;
import com.boun.swe.semnet.commons.exception.SemNetValidationException;
import com.boun.swe.semnet.commons.type.ErrorCode;

public abstract class BaseService {


    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void validateFields(Object request) throws SemNetValidationException {
//        Set<? extends ConstraintViolation<?>> constraintViolations = validator.validate(request);
//        if (constraintViolations.size() > 0) {
//            throw new SemNetValidationException(constraintViolations);
//        }
    }
    
    protected void validate(BaseRequest request) throws SemNetRuntimeException{
    	
    	if(request.getAuthToken() == null || "".equalsIgnoreCase(request.getAuthToken())){
    		throw new SemNetRuntimeException(400, ErrorCode.INVALID_INPUT, "Authentication token field is empty", "");
    	}
    	
    	validateFields(request);
    }

    protected void validate(String authToken) throws SemNetRuntimeException{

        BaseRequest request = new BaseRequest();
        request.setAuthToken(authToken);

        validate(request);
    }
}
