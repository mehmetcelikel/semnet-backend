package com.boun.swe.semnet.api.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.boun.swe.semnet.commons.data.request.AuthenticationRequest;
import com.boun.swe.semnet.commons.data.request.BaseRequest;
import com.boun.swe.semnet.commons.data.request.BasicQueryRequest;
import com.boun.swe.semnet.commons.data.request.BasicSearchRequest;
import com.boun.swe.semnet.commons.data.request.ChangePasswordRequest;
import com.boun.swe.semnet.commons.data.request.CreateUserRequest;
import com.boun.swe.semnet.commons.data.request.ResetPasswordRequest;
import com.boun.swe.semnet.commons.data.request.UpdateUserRequest;
import com.boun.swe.semnet.commons.data.response.ActionResponse;
import com.boun.swe.semnet.commons.data.response.CreateResponse;
import com.boun.swe.semnet.commons.data.response.GetUserResponse;
import com.boun.swe.semnet.commons.data.response.LoginResponse;
import com.boun.swe.semnet.commons.data.response.UserListResponse;
import com.boun.swe.semnet.commons.exception.SemNetException;
import com.boun.swe.semnet.commons.type.ErrorCode;
import com.boun.swe.semnet.sevices.service.UserService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import shaded.org.apache.commons.io.IOUtils;

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
    public @ResponseBody CreateResponse createUser(@RequestBody CreateUserRequest request) {

    	try{
    		return userService.create(request);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running createUser service, code->" + e.getErrorCode());
    		
    		return new CreateResponse(e.getErrorCode(), e.getErrors());
    	}
    }
    
    @ApiOperation(value="Upload Profile Image")
    @RequestMapping(value="upload", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody ActionResponse uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("userId")String userId, @RequestParam("authToken")String authToken) {

    	try{
    		return userService.uploadProfileImage(authToken, file.getBytes(), file.getOriginalFilename(), userId);
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running uploadImage service, code->" + e.getErrorCode());
    		
    		return new ActionResponse(e.getErrorCode(), e.getErrors());
    	}catch (IOException e) {
    		
    		logger.error("IO Error occured while running uploadImage service", e);
    		
    		return new ActionResponse(ErrorCode.CORRUPTED_IMAGE, null);
		}
    }
    
	@ApiOperation(value = "Download Profile Image")
	@RequestMapping(value = "download", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody
	void download(@RequestParam("userId")String userId, @RequestParam("authToken")String authToken,  HttpServletResponse response) {

		try {

			byte[] image =  userService.downloadProfileImage(authToken, userId);

			response.setContentType("application/force-download");
			response.setContentLength((int) image.length);
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Disposition","attachment; filename=\"" + "ProfileImage" +"\"");

			IOUtils.copy(new ByteArrayInputStream(image), response.getOutputStream());
			
		}catch (SemNetException e) {
    		logger.error("Error occured while running downloadProfileImage service, code->" + e.getErrorCode());
		} catch (Exception e) {
			logger.error("IO Error occured while running downloadProfileImage service", e);
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
    
    @ApiOperation(value="Query All Users")
    @RequestMapping(value="queryAllUsers", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success")})
    public @ResponseBody UserListResponse queryAllUsers(@RequestBody BaseRequest request){
    	
    	try{
    		return userService.getAllUserList(request);	
    		
    	}catch (SemNetException e) {
    		
    		logger.error("Error occured while running getAllUserList service, code->" + e.getErrorCode());
			
    		return new UserListResponse(e.getErrorCode(), e.getErrors());
    	}
    }
}
