package com.boun.swe.semnet.api.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.boun.swe.semnet.commons.data.request.BasicSearchRequest;
import com.boun.swe.semnet.commons.data.response.QueryLabelResponse;
import com.boun.swe.semnet.commons.exception.SemNetException;
import com.boun.swe.semnet.sevices.service.SemanticSearchService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value = "search", description = "Search service")
@RequestMapping("/v1/search")
public class SearchController {

	private final static Logger logger = LoggerFactory.getLogger(SearchController.class);
	
    @Autowired
    private SemanticSearchService searchService;
    
    @ApiOperation(value="Query Label")
    @RequestMapping(value="queryLabel", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody QueryLabelResponse queryLabel(@RequestBody BasicSearchRequest request){
    	
    	try{
    		return searchService.queryLabel(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running queryLabel service, code->" + e.getErrorCode());
			
    		return new QueryLabelResponse(e.getErrorCode(), e.getErrors());
    	}
    }
}
