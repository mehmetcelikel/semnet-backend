package com.boun.swe.semnet.commons.data.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionResponse {

    @JsonProperty("ack")
    private boolean acknowledge;
    
    private String message;

    public ActionResponse(){}

    public ActionResponse(boolean acknowledge){
        this.acknowledge = acknowledge;
    }
}
