package com.boun.swe.semnet.api.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
import com.boun.swe.semnet.commons.data.response.SearchUserResponse;
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
    		if(logger.isDebugEnabled()){
    			logger.debug("createUser request received, request->" + request.toString());
    		}
    		return userService.create(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running createUser service, code->" + e.getErrorCode());
    		
    		return new CreateUserResponse(e.getErrorCode());
    		
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("createUser operation finished");
    		}
    	}
    }

	@ApiOperation(value="Update User")
	@RequestMapping(value="update", method = RequestMethod.POST)
	@ApiResponses(value={@ApiResponse(code=200, message = "Success")})
	public @ResponseBody ActionResponse updateUser(@RequestBody UpdateUserRequest request) {

		try{
			if(logger.isDebugEnabled()){
				logger.debug("updateUser request received, request->" + request.toString());
			}
			return userService.update(request);
		}catch (SemNetException e) {
    		
			logger.error("Error occured while running updateUser service, code->" + e.getErrorCode());
			
    		return new ActionResponse(e.getErrorCode());
    		
		}finally{
			if(logger.isDebugEnabled()){
				logger.debug("updateUser operation finished");
			}
		}
	}

    @ApiOperation(value="Login")
    @RequestMapping(value="login", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody LoginResponse authenticate(@RequestBody AuthenticationRequest request){
    	
    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("login request received, request->" + request.toString());
    		}
    		return userService.login(request);	
    	}catch (SemNetException e) {
    		
			logger.error("Error occured while running authenticate service, code->" + e.getErrorCode());
			
    		return new LoginResponse(e.getErrorCode());
    		
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("login operation finished, username->" + request.getUsername());
    		}
    	}
    }
    
    @ApiOperation(value="Logout")
    @RequestMapping(value="logout", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse logout(@RequestBody BaseRequest request){
    	
    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("logout request received, request->" + request.toString());
    		}
    		return userService.logout(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running logout service, code->" + e.getErrorCode());
			
    		return new ActionResponse(e.getErrorCode());
    		
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("logout operation finished, username->" + request.getAuthToken());
    		}
    	}
    }
 
    @ApiOperation(value="Reset Password")
    @RequestMapping(value="resetPassword", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse resetPassword(@RequestBody ResetPasswordRequest request){
    	
    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("resetPassword request received, request->" + request.toString());
    		}
    		return userService.resetPassword(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running resetPassword service, code->" + e.getErrorCode());
			
    		return new ActionResponse(e.getErrorCode());
    		
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("resetPassword operation finished, username->" + request.getUsername());
    		}
    	}
    }

    @ApiOperation(value="Change Password")
    @RequestMapping(value="changePassword", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse changePassword(@RequestBody ChangePasswordRequest request){
    	
    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("changePassword request received, oneTimeToken->" + request.getOneTimeToken());
    		}
    		return userService.changePassword(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running changePassword service, code->" + e.getErrorCode());
			
    		return new ActionResponse(e.getErrorCode());
    		
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("changePassword operation finished, oneTimeToken->" + request.getOneTimeToken());
    		}
    	}
    }
    
    @ApiOperation(value="Get User with ID")
    @RequestMapping(value="get", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody GetUserResponse getUser(@RequestBody BasicQueryRequest request){
    	
    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("getUser request received");
    		}
    		return userService.get(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running getUser service, code->" + e.getErrorCode());
			
    		return new GetUserResponse(e.getErrorCode());
    		
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("getUser operation finished");
    		}
    	}
    }
    
    @ApiOperation(value="Query User")
    @RequestMapping(value="query", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody SearchUserResponse searchUser(@RequestBody BasicSearchRequest request){
    	
    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("searchUser request received");
    		}
    		return userService.search(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running searchUser service, code->" + e.getErrorCode());
			
    		return new SearchUserResponse(e.getErrorCode());
    		
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("searchUser operation finished");
    		}
    	}
    }
}
