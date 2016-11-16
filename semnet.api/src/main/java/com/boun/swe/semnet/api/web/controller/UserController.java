package com.boun.swe.semnet.api.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.boun.swe.semnet.commons.data.request.FriendRequest;
import com.boun.swe.semnet.commons.data.request.AuthenticationRequest;
import com.boun.swe.semnet.commons.data.request.BaseRequest;
import com.boun.swe.semnet.commons.data.request.BasicQueryRequest;
import com.boun.swe.semnet.commons.data.request.BasicSearchRequest;
import com.boun.swe.semnet.commons.data.request.ChangePasswordRequest;
import com.boun.swe.semnet.commons.data.request.CreateUserRequest;
import com.boun.swe.semnet.commons.data.request.ResetPasswordRequest;
import com.boun.swe.semnet.commons.data.request.UpdateUserRequest;
import com.boun.swe.semnet.commons.data.response.ActionResponse;
import com.boun.swe.semnet.commons.data.response.CreateUserResponse;
import com.boun.swe.semnet.commons.data.response.GetUserResponse;
import com.boun.swe.semnet.commons.data.response.LoginResponse;
import com.boun.swe.semnet.commons.data.response.UserListResponse;
import com.boun.swe.semnet.commons.exception.SemNetException;
import com.boun.swe.semnet.sevices.service.UserService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value = "user", description = "User service")
@RequestMapping("/v1/user")
public class UserController {

	private final static Logger logger = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
    private UserService userService;

    @ApiOperation(value="Create User")
    @RequestMapping(value="create", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody CreateUserResponse createUser(@RequestBody CreateUserRequest request) {

    	try{
    		return userService.create(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running createUser service, code->" + e.getErrorCode());
    		
    		return new CreateUserResponse(e.getErrorCode(), e.getErrors());
    	}
    }

	@ApiOperation(value="Update User")
	@RequestMapping(value="update", method = RequestMethod.POST)
	@ApiResponses(value={@ApiResponse(code=200, message = "Success")})
	public @ResponseBody ActionResponse updateUser(@RequestBody UpdateUserRequest request) {

		try{
			return userService.update(request);
		}catch (SemNetException e) {
    		
			logger.error("Error occured while running updateUser service, code->" + e.getErrorCode());
			
    		return new ActionResponse(e.getErrorCode(), e.getErrors());
		}
	}

    @ApiOperation(value="Login")
    @RequestMapping(value="login", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody LoginResponse authenticate(@RequestBody AuthenticationRequest request){
    	
    	try{
    		return userService.login(request);	
    	}catch (SemNetException e) {
    		
			logger.error("Error occured while running authenticate service, code->" + e.getErrorCode());
			
    		return new LoginResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Logout")
    @RequestMapping(value="logout", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse logout(@RequestBody BaseRequest request){
    	
    	try{
    		return userService.logout(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running logout service, code->" + e.getErrorCode());
			
    		return new ActionResponse(e.getErrorCode(), e.getErrors());
    	}
    }
 
    @ApiOperation(value="Reset Password")
    @RequestMapping(value="resetPassword", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse resetPassword(@RequestBody ResetPasswordRequest request){
    	
    	try{
    		return userService.resetPassword(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running resetPassword service, code->" + e.getErrorCode());
			
    		return new ActionResponse(e.getErrorCode(), e.getErrors());
    	}
    }

    @ApiOperation(value="Change Password")
    @RequestMapping(value="changePassword", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse changePassword(@RequestBody ChangePasswordRequest request){
    	
    	try{
    		return userService.changePassword(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running changePassword service, code->" + e.getErrorCode());
			
    		return new ActionResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Get User with ID")
    @RequestMapping(value="get", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody GetUserResponse getUser(@RequestBody BasicQueryRequest request){
    	
    	try{
    		return userService.get(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running getUser service, code->" + e.getErrorCode());
			
    		return new GetUserResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Query User")
    @RequestMapping(value="query", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody UserListResponse searchUser(@RequestBody BasicSearchRequest request){
    	
    	try{
    		return userService.search(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running searchUser service, code->" + e.getErrorCode());
			
    		return new UserListResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Add Friend")
    @RequestMapping(value="addFriend", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse addFriend(@RequestBody FriendRequest request){
    	
    	try{
    		return userService.addFriend(request);	
    		
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
    		return userService.removeFriend(request);	
    		
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
    		return userService.blockFriend(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running blockFriend service, code->" + e.getErrorCode());
			
    		return new ActionResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="List Friends")
    @RequestMapping(value="listFriends", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody UserListResponse listFriends(@RequestBody BaseRequest request){
    	
    	try{
    		return userService.listFriends(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running listFriends service, code->" + e.getErrorCode());
			
    		return new UserListResponse(e.getErrorCode(), e.getErrors());
    	}
    }
}
