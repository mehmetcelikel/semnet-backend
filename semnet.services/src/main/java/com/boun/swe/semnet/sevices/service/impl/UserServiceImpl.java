package com.boun.swe.semnet.sevices.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.swe.semnet.commons.data.request.AuthenticationRequest;
import com.boun.swe.semnet.commons.data.request.BaseRequest;
import com.boun.swe.semnet.commons.data.request.BasicQueryRequest;
import com.boun.swe.semnet.commons.data.request.BasicSearchRequest;
import com.boun.swe.semnet.commons.data.request.ChangePasswordRequest;
import com.boun.swe.semnet.commons.data.request.CreateUserRequest;
import com.boun.swe.semnet.commons.data.request.FriendRequest;
import com.boun.swe.semnet.commons.data.request.ResetPasswordRequest;
import com.boun.swe.semnet.commons.data.request.UpdateUserRequest;
import com.boun.swe.semnet.commons.data.response.ActionResponse;
import com.boun.swe.semnet.commons.data.response.CreateUserResponse;
import com.boun.swe.semnet.commons.data.response.GetUserResponse;
import com.boun.swe.semnet.commons.data.response.LoginResponse;
import com.boun.swe.semnet.commons.data.response.UserListResponse;
import com.boun.swe.semnet.commons.exception.SemNetException;
import com.boun.swe.semnet.commons.type.ErrorCode;
import com.boun.swe.semnet.commons.type.UserStatus;
import com.boun.swe.semnet.commons.util.KeyUtils;
import com.boun.swe.semnet.sevices.db.model.Friendship;
import com.boun.swe.semnet.sevices.db.model.User;
import com.boun.swe.semnet.sevices.db.repo.FriendshipRepository;
import com.boun.swe.semnet.sevices.db.repo.UserRepository;
import com.boun.swe.semnet.sevices.service.BaseService;
import com.boun.swe.semnet.sevices.service.LoginService;
import com.boun.swe.semnet.sevices.service.UserService;
import com.boun.swe.semnet.sevices.session.SemNetSession;

@Service
public class UserServiceImpl extends BaseService implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private FriendshipRepository friendshipRepository;
	
	@Override
	public CreateUserResponse create(CreateUserRequest request) {

		validateFields(request);
		
		User user = userRepository.findByUsername(request.getUsername());
		if(user != null){
			throw new SemNetException(ErrorCode.DUPLICATE_USER);
		}

		user = userRepository.merge(mapUser(request));

		return new CreateUserResponse(ErrorCode.SUCCESS, user.getId(), user.getUsername(), user.getFirstname(), user.getLastname());
	}

	@Override
	public GetUserResponse get(BasicQueryRequest request){
		
		validate(request);
		
		User user = userRepository.findById(request.getId());
		
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
		
		List<User> userlist = userRepository.searchUser(request.getQueryString());
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
		
		//TODO only admins and profile owners should be able to updateUser

		User authenticatedUser = SemNetSession.getInstance().getUser(request.getAuthToken());
		if(!authenticatedUser.getId().equalsIgnoreCase(request.getId())){
			throw new SemNetException(ErrorCode.INVALID_INPUT);
		}
		
		User user =	userRepository.findById(request.getId());

		user.setFirstname(request.getFirstname());
		user.setLastname(request.getLastname());

		userRepository.merge(user);

		return new ActionResponse(ErrorCode.SUCCESS);
	}

	@Override
	public LoginResponse login(AuthenticationRequest request) {

		// TODO password must be stored encrypted
		User user = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());

		if (user == null) {
			throw new SemNetException(ErrorCode.USER_NOT_FOUND);
		}

		LoginResponse response = new LoginResponse(ErrorCode.SUCCESS);
		response.setToken(loginService.login(user).getToken());
		response.setId(user.getId());
		
		return response;
	}
	
	@Override
	public ActionResponse logout(BaseRequest request) {

		validate(request);
		
		User authenticatedUser = SemNetSession.getInstance().getUser(request.getAuthToken());
		loginService.logout(authenticatedUser);
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}

	@Override
	public ActionResponse resetPassword(final ResetPasswordRequest request) {
		
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null){
			throw new SemNetException(ErrorCode.USER_NOT_FOUND);
		}
		
		final String oneTimeToken = KeyUtils.currentTimeUUID().toString();
		user.setOneTimeToken(oneTimeToken);
		userRepository.save(user);
		
//		mailService.sendMail(request.getUsername(), "Password reset request", "You token for password renewal is " + oneTimeToken);
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}

	@Override
	public ActionResponse changePassword(ChangePasswordRequest request) {
		
		final User user = userRepository.findByOneTimeToken(request.getOneTimeToken());
		if(user == null){
			throw new SemNetException(ErrorCode.USER_NOT_FOUND);
		}
		
		user.setOneTimeToken(null);
		user.setPassword(request.getNewPassword());
		
		userRepository.save(user);

//		mailService.sendMail(user.getUsername(), "Password change request", "You password is updated successfully");
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}

	@Override
	public ActionResponse addFriend(FriendRequest request) {
		validate(request);
		
		User user = userRepository.findById(request.getFriendId());
		if(user == null){
			throw new SemNetException(ErrorCode.USER_NOT_FOUND);
		}
		
		User authenticatedUser = SemNetSession.getInstance().getUser(request.getAuthToken());
		if(request.getFriendId().equals(authenticatedUser.getId())){
			throw new SemNetException(ErrorCode.CANNOT_ADD_SAME_USER_AS_FRIEND);
		}
		
		List<Friendship> friendList = friendshipRepository.findById(authenticatedUser.getId());
		if(friendList != null && !friendList.isEmpty()){
			Optional<Friendship> friendship = friendList.stream().filter(x -> x.getTarget().getId().equals(request.getFriendId())).findFirst();
			if(friendship.isPresent()){
				throw new SemNetException(ErrorCode.DUPLICATE_FRIEND);
			}
		}
		Friendship friendship = new Friendship();
		friendship.setActive(true);
		friendship.setCreationTime(new Date());
		friendship.setSource(authenticatedUser);
		friendship.setTarget(user);
		
		friendshipRepository.merge(friendship, friendList);
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}
	
	@Override
	public ActionResponse removeFriend(FriendRequest request) {
		validate(request);
		
		User authenticatedUser = SemNetSession.getInstance().getUser(request.getAuthToken());
		
		Friendship friend = null;
		
		List<Friendship> friendList = friendshipRepository.findById(authenticatedUser.getId());
		if(friendList != null && !friendList.isEmpty()){
			Optional<Friendship> friendship = friendList.stream().filter(x -> x.getTarget().getId().equals(request.getFriendId())).findFirst();
			if(friendship.isPresent()){
				friend = friendship.get(); 
			}
		}
		
		if(friend == null){
			throw new SemNetException(ErrorCode.FRIEND_NOT_FOUND);
		}
		
		friendshipRepository.delete(friend, friendList);
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}
	
	@Override
	public ActionResponse blockFriend(FriendRequest request) {
		validate(request);
		
		User authenticatedUser = SemNetSession.getInstance().getUser(request.getAuthToken());
		
		Friendship friend = null;
		
		List<Friendship> friendList = friendshipRepository.findById(authenticatedUser.getId());
		if(friendList != null && !friendList.isEmpty()){
			Optional<Friendship> friendship = friendList.stream().filter(x -> x.getTarget().getId().equals(request.getFriendId())).findFirst();
			if(friendship.isPresent()){
				friend = friendship.get(); 
			}
		}
		if(friend == null){
			throw new SemNetException(ErrorCode.FRIEND_NOT_FOUND);
		}
		
		if(!friend.isActive()){
			throw new SemNetException(ErrorCode.FRIEND_NOT_FOUND);
		}
		
		friend.setActive(false);
		friendshipRepository.merge(friend, friendList);
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}
	
	@Override
	public UserListResponse listFriends(BaseRequest request) {
		validate(request);
		
		User authenticatedUser = SemNetSession.getInstance().getUser(request.getAuthToken());
		
		UserListResponse response = new UserListResponse(ErrorCode.SUCCESS);
		
		List<Friendship> friendshipList = friendshipRepository.findById(authenticatedUser.getId());
		if(friendshipList == null || friendshipList.isEmpty()){
			return response;
		}

		friendshipList.stream().filter(x -> x.isActive()).forEach(y -> response.addUser(y.getTarget().getId(), y.getTarget().getUsername(), y.getTarget().getFirstname(), y.getTarget().getLastname()));
		
		return response;
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
}
