package com.boun.swe.semnet.api.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.boun.swe.semnet.commons.data.request.BaseRequest;
import com.boun.swe.semnet.commons.data.request.BasicQueryRequest;
import com.boun.swe.semnet.commons.data.request.FriendRequest;
import com.boun.swe.semnet.commons.data.response.ActionResponse;
import com.boun.swe.semnet.commons.data.response.UserListResponse;
import com.boun.swe.semnet.commons.exception.SemNetException;
import com.boun.swe.semnet.sevices.service.FriendService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value = "friend", description = "Friend service")
@RequestMapping("/v1/friend")
public class FriendController {

	private final static Logger logger = LoggerFactory.getLogger(FriendController.class);
	
    @Autowired
    private FriendService friendService;
    
    @ApiOperation(value="Add Friend")
    @RequestMapping(value="addFriend", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse addFriend(@RequestBody FriendRequest request){
    	
    	try{
    		return friendService.addFriend(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running addFriend service, code->" + e.getErrorCode());
			
    		return new ActionResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Remove Friend")
    @RequestMapping(value="removeFriend", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse removeFriend(@RequestBody FriendRequest request){
    	
    	try{
    		return friendService.removeFriend(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running removeFriend service, code->" + e.getErrorCode());
			
    		return new ActionResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Block Friend")
    @RequestMapping(value="blockFriend", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse blockFriend(@RequestBody FriendRequest request){
    	
    	try{
    		return friendService.blockFriend(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running blockFriend service, code->" + e.getErrorCode());
			
    		return new ActionResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="List Friends")
    @RequestMapping(value="listFriends", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody UserListResponse listFriends(@RequestBody BasicQueryRequest request){
    	
    	try{
    		return friendService.listFriends(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running listFriends service, code->" + e.getErrorCode());
			
    		return new UserListResponse(e.getErrorCode(), e.getErrors());
    	}
    }
}
