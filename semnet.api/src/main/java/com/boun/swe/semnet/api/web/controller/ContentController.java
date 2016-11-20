package com.boun.swe.semnet.api.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.boun.swe.semnet.commons.data.request.AddCommentRequest;
import com.boun.swe.semnet.commons.data.request.AddContentRequest;
import com.boun.swe.semnet.commons.data.request.BasicQueryRequest;
import com.boun.swe.semnet.commons.data.request.ListContentRequest;
import com.boun.swe.semnet.commons.data.response.ActionResponse;
import com.boun.swe.semnet.commons.data.response.ContentListResponse;
import com.boun.swe.semnet.commons.data.response.GetContentResponse;
import com.boun.swe.semnet.commons.exception.SemNetException;
import com.boun.swe.semnet.sevices.service.ContentService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value = "content", description = "Content service")
@RequestMapping("/v1/content")
public class ContentController {

	private final static Logger logger = LoggerFactory.getLogger(ContentController.class);
	
    @Autowired
    private ContentService contentService;

    @ApiOperation(value="Create Content")
    @RequestMapping(value="create", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse createContent(@RequestBody AddContentRequest request) {

    	try{
    		return contentService.create(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running createContent service, code->" + e.getErrorCode());
    		
    		return new ActionResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Get Content")
    @RequestMapping(value="get", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody GetContentResponse getContent(@RequestBody BasicQueryRequest request) {

    	try{
    		return contentService.get(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running getContent service, code->" + e.getErrorCode());
    		
    		return new GetContentResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="List Content")
    @RequestMapping(value="list", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ContentListResponse listContent(@RequestBody ListContentRequest request) {

    	try{
    		return contentService.list(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running listContent service, code->" + e.getErrorCode());
    		
    		return new ContentListResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Like Content")
    @RequestMapping(value="like", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse likeContent(@RequestBody BasicQueryRequest request) {

    	try{
    		return contentService.like(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running likeContent service, code->" + e.getErrorCode());
    		
    		return new ContentListResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Unlike Content")
    @RequestMapping(value="unlike", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse unlikeContent(@RequestBody BasicQueryRequest request) {

    	try{
    		return contentService.unLike(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running unLikeContent service, code->" + e.getErrorCode());
    		
    		return new ContentListResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Add Comment to a Content")
    @RequestMapping(value="addComment", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse addComment(@RequestBody AddCommentRequest request) {

    	try{
    		return contentService.addComment(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running addComment service, code->" + e.getErrorCode());
    		
    		return new ContentListResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Remove Comment from a Content")
    @RequestMapping(value="removeComment", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse removeComment(@RequestBody AddCommentRequest request) {

    	try{
    		return contentService.addComment(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running removeComment service, code->" + e.getErrorCode());
    		
    		return new ContentListResponse(e.getErrorCode(), e.getErrors());
    	}
    }
}
