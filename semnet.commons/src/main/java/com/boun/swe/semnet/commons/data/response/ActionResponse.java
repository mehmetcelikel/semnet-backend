package com.boun.swe.semnet.commons.data.response;

import com.boun.swe.semnet.commons.type.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionResponse {

    private String errorCode;
    private String message;

    public ActionResponse(ErrorCode code){
        this.errorCode = code.getFormattedCode();
        this.message = code.getMessage();
    }
}
