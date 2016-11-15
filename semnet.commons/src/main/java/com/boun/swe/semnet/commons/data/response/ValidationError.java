package com.boun.swe.semnet.commons.data.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationError {

    private String propertyName;
    private String message;

}
