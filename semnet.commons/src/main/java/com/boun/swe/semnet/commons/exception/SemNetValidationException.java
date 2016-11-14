package com.boun.swe.semnet.commons.exception;

import javax.validation.ConstraintViolation;

import com.boun.swe.semnet.commons.data.response.ErrorResponse;
import com.boun.swe.semnet.commons.data.response.ValidationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SemNetValidationException extends RuntimeException {

    private final int status = 400;
    private final String errorCode = "100";
    private String errorMessage;
    private String developerMessage;
    private List<ValidationError> errors = new ArrayList<ValidationError>();

    public SemNetValidationException() {
        errorMessage = "Validation Error";
        developerMessage = "The data passed in the request was invalid. Please check and resubmit";
    }

    public SemNetValidationException(String message) {
        super();
        errorMessage = message;
    }

    public SemNetValidationException(Set<? extends ConstraintViolation<?>> violations) {
        this();
        for(ConstraintViolation<?> constraintViolation : violations) {
            ValidationError error = new ValidationError();
            error.setMessage(constraintViolation.getMessage());
            error.setPropertyName(constraintViolation.getPropertyPath().toString());
            error.setPropertyValue(constraintViolation.getInvalidValue() != null ? constraintViolation.getInvalidValue().toString() : null);
            errors.add(error);
        }
    }

//    public Response getResponse() {
//        return new Response(status, getErrorResponse());
//    }

    public ErrorResponse getErrorResponse() {
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(errorCode);
        response.setApplicationMessage(developerMessage);
        response.setConsumerMessage(errorMessage);
        response.setValidationErrors(errors);
        return response;
    }

}

