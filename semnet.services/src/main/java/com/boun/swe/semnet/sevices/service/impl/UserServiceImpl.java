package com.boun.swe.semnet.sevices.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.boun.swe.semnet.commons.type.UserStatus;
import com.boun.swe.semnet.commons.util.KeyUtils;
import com.boun.swe.semnet.sevices.db.manager.ImagePersistencyManager;
import com.boun.swe.semnet.sevices.db.model.Content;
import com.boun.swe.semnet.sevices.db.model.User;
import com.boun.swe.semnet.sevices.service.BaseService;
import com.boun.swe.semnet.sevices.service.UserService;

@Service
public class UserServiceImpl extends BaseService implements UserService {

	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private ImagePersistencyManager imagePersistencyManager;
	
	@Override
	public CreateResponse create(CreateUserRequest request) {

		validateFields(request);
		
		User user = userManager.findByUsername(request.getUsername());
		if(user != null){
			throw new SemNetException(ErrorCode.DUPLICATE_USER);
		}

		user = userManager.merge(mapUser(request));

		return new CreateResponse(ErrorCode.SUCCESS, user.getId());
	}

	@Override
	public GetUserResponse get(BasicQueryRequest request){
		
		validate(request);
		
		User user = userManager.findById(request.getId());
		
		GetUserResponse response = new GetUserResponse(ErrorCode.SUCCESS);
		response.setUsername(user.getUsername());
		response.setFirstname(user.getFirstname());
		response.setLastname(user.getLastname());
		response.setStatus(user.getStatus());
		
		return response;
	}
	
	@Override
	public UserListResponse search(BasicSearchRequest request){
		
		validate(request);
		
		List<User> userlist = userManager.searchUser(request.getQueryString());
		if(userlist == null || userlist.isEmpty()){
			throw new SemNetException(ErrorCode.USER_NOT_FOUND);
		}
		
		UserListResponse response = new UserListResponse(ErrorCode.SUCCESS);
		for (User user : userlist) {
			response.addUser(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname());
		}
		
		return response;
	}
	
	@Override
	public ActionResponse update(UpdateUserRequest request) {

		validate(request);
		
		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		if(!authenticatedUser.getId().equalsIgnoreCase(request.getId())){
			throw new SemNetException(ErrorCode.INVALID_INPUT);
		}
		
		User user =	userManager.findById(request.getId());

		user.setFirstname(request.getFirstname());
		user.setLastname(request.getLastname());

		userManager.merge(user);

		return new ActionResponse(ErrorCode.SUCCESS);
	}

	@Override
	public LoginResponse login(AuthenticationRequest request) {

		// TODO password must be stored encrypted
		User user = userManager.findByUsername(request.getUsername());
		if (user == null) {
			throw new SemNetException(ErrorCode.USER_NOT_FOUND);
		}
		
		if(!user.getPassword().equals(request.getPassword())){
			throw new SemNetException(ErrorCode.USER_NOT_FOUND);
		}

		LoginResponse response = new LoginResponse(ErrorCode.SUCCESS);
		response.setId(user.getId());
		
		String token = user.getToken();
		if(token == null){
			
			logger.info("Token is null");
			
			token = KeyUtils.currentTimeUUID().toString();
			user = userManager.login(token, user);
			
			userManager.updateCache(user);
			
		}else{
			
			logger.info("Token is not null:" + token);
			
			User cachedUser = userManager.login(token, null);
			if(cachedUser == null){
				
				logger.info("User is null:" + token);
				
				token = KeyUtils.currentTimeUUID().toString();
				user = userManager.login(token, user);
				
				logger.info("User is null newToken:" + token);
				
				userManager.updateCache(user);
			}
		}
		
		response.setToken(token);
		
		return response;
	}
	
	@Override
	public ActionResponse logout(BaseRequest request) {

		validate(request);
		
		userManager.logout(request.getAuthToken());
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}

	@Override
	public ActionResponse resetPassword(final ResetPasswordRequest request) {
		
		User user = userManager.findByUsername(request.getUsername());
		if(user == null){
			throw new SemNetException(ErrorCode.USER_NOT_FOUND);
		}
		
		final String oneTimeToken = KeyUtils.currentTimeUUID().toString();
		user.setOneTimeToken(oneTimeToken);
		userManager.merge(user);
		
//		mailService.sendMail(request.getUsername(), "Password reset request", "You token for password renewal is " + oneTimeToken);
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}

	@Override
	public ActionResponse changePassword(ChangePasswordRequest request) {
		
		final User user = userManager.findByOneTimeToken(request.getOneTimeToken());
		if(user == null){
			throw new SemNetException(ErrorCode.USER_NOT_FOUND);
		}
		
		user.setOneTimeToken(null);
		user.setPassword(request.getNewPassword());
		
		userManager.merge(user);

//		mailService.sendMail(user.getUsername(), "Password change request", "You password is updated successfully");
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}

	private User mapUser(CreateUserRequest request){
		User user = new User();
		
		user.setFirstname(request.getFirstname());
		user.setLastname(request.getLastname());
		user.setPassword(request.getPassword());
		user.setStatus(UserStatus.ACTIVE);
		user.setUsername(request.getUsername());
		
		return user;
	}
	
	@Override
	public ActionResponse uploadProfileImage(String authToken, byte[] image, String filename, String userId) {
		
		BaseRequest request = new BaseRequest();
		request.setAuthToken(authToken);
		
		validate(request);
		
		User user = userManager.findById(userId);
		if(user == null){
			throw new SemNetException(ErrorCode.CONTENT_NOT_FOUND);
		}
		
		imagePersistencyManager.saveProfileImage(userId, image);
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}
	
	@Override
	public byte[] downloadProfileImage(String authToken, String userId) {
		
		BaseRequest request = new BaseRequest();
		request.setAuthToken(authToken);
		
		validate(request);
		
		return imagePersistencyManager.getProfileImage(userId);
	}
}
