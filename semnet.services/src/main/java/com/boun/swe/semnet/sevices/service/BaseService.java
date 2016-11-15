package com.boun.swe.semnet.sevices.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boun.swe.semnet.commons.data.request.BaseRequest;
import com.boun.swe.semnet.commons.exception.SemNetException;
import com.boun.swe.semnet.commons.type.ErrorCode;
import com.boun.swe.semnet.sevices.session.SemNetSession;

public abstract class BaseService {


    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void validateFields(Object request) throws SemNetException {
    	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<? extends ConstraintViolation<?>> constraintViolations = validator.validate(request);
        if (constraintViolations.size() > 0) {
            SemNetException ex = new SemNetException(ErrorCode.VALIDATION_ERROR);
            ex.setValidationError(constraintViolations);
            throw ex;
        }
    }
    
    protected void validate(BaseRequest request) throws SemNetException{
    	
    	if(request.getAuthToken() == null || "".equalsIgnoreCase(request.getAuthToken())){
    		throw new SemNetException(ErrorCode.INVALID_INPUT, "Authentication token field is empty");
    	}
    	
    	if (!SemNetSession.getInstance().validateToken(request.getAuthToken())) {
    		throw new SemNetException(ErrorCode.OPERATION_NOT_ALLOWED, "");
		}
    	
    	validateFields(request);
    }
}
