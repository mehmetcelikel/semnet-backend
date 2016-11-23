package com.boun.swe.semnet.sevices.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.swe.semnet.commons.data.request.BaseRequest;
import com.boun.swe.semnet.commons.data.request.FriendRequest;
import com.boun.swe.semnet.commons.data.response.ActionResponse;
import com.boun.swe.semnet.commons.data.response.UserListResponse;
import com.boun.swe.semnet.commons.exception.SemNetException;
import com.boun.swe.semnet.commons.type.ErrorCode;
import com.boun.swe.semnet.sevices.db.model.Friendship;
import com.boun.swe.semnet.sevices.db.model.User;
import com.boun.swe.semnet.sevices.db.repo.FriendshipRepository;
import com.boun.swe.semnet.sevices.db.repo.UserRepository;
import com.boun.swe.semnet.sevices.service.BaseService;
import com.boun.swe.semnet.sevices.service.FriendService;
import com.boun.swe.semnet.sevices.session.SemNetSession;

@Service
public class FriendServiceImpl extends BaseService implements FriendService{

	@Autowired
	private FriendshipRepository friendshipRepository;
	
	@Autowired
	private UserRepository userRepository;
	
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
}