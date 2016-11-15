package com.boun.swe.semnet.commons.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.boun.swe.semnet.commons.data.response.ValidationError;
import com.boun.swe.semnet.commons.type.ErrorCode;

public class SemNetException extends RuntimeException {

	private static final long serialVersionUID = -156081897455499864L;

	private ErrorCode errorCode;
	private String additionalInfo;
	
	private List<ValidationError> errors = new ArrayList<ValidationError>();
	
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
    
    public void setValidationError(Set<? extends ConstraintViolation<?>> violations) {
        for(ConstraintViolation<?> constraintViolation : violations) {
            ValidationError error = new ValidationError();
            error.setMessage(constraintViolation.getMessage());
            error.setPropertyName(constraintViolation.getPropertyPath().toString());
            errors.add(error);
        }
    }

    public List<ValidationError> getErrors() {
		return errors;
	}
}
