package com.boun.swe.semnet.sevices.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.boun.swe.semnet.commons.data.request.BasicQueryRequest;
import com.boun.swe.semnet.commons.data.request.FriendRequest;
import com.boun.swe.semnet.commons.data.response.ActionResponse;
import com.boun.swe.semnet.commons.data.response.UserListResponse;
import com.boun.swe.semnet.commons.exception.SemNetException;
import com.boun.swe.semnet.commons.type.ErrorCode;
import com.boun.swe.semnet.commons.util.KeyUtils;
import com.boun.swe.semnet.sevices.db.model.Friendship;
import com.boun.swe.semnet.sevices.db.model.User;
import com.boun.swe.semnet.sevices.service.BaseService;
import com.boun.swe.semnet.sevices.service.FriendService;

@Service
public class FriendServiceImpl extends BaseService implements FriendService{

	private final static Logger logger = LoggerFactory.getLogger(FriendServiceImpl.class);
	
	@Override
	public ActionResponse addFriend(FriendRequest request) {
		validate(request);
		
		User user = userManager.findById(request.getFriendId());
		if(user == null){
			throw new SemNetException(ErrorCode.USER_NOT_FOUND);
		}
		
		User authenticatedUser = userManager.login(request.getAuthToken(), user);
		authenticatedUser = userManager.findById(authenticatedUser.getId());
		
		if(request.getFriendId().equals(authenticatedUser.getId())){
			throw new SemNetException(ErrorCode.CANNOT_ADD_SAME_USER_AS_FRIEND);
		}
		
		List<Friendship> friendList = authenticatedUser.getFriendList();
		if(friendList != null && !friendList.isEmpty()){
			Optional<Friendship> friendship = friendList.stream().filter(x -> x.getUserId().equals(request.getFriendId())).findFirst();
			if(friendship.isPresent()){
				throw new SemNetException(ErrorCode.DUPLICATE_FRIEND);
			}
		}
		Friendship friendship = new Friendship();
		friendship.setActive(true);
		friendship.setCreationTime(new Date());
		friendship.setUserId(user.getId());
		friendship.setId(KeyUtils.currentTimeUUID().toString());
		
		authenticatedUser.getFriendList().add(friendship);
		
		userManager.merge(authenticatedUser);
		
		logger.info("addFriend->" + authenticatedUser.getId() + ", FriendCount->" + authenticatedUser.getFriendList().size());
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}
	
	@Override
	public ActionResponse removeFriend(FriendRequest request) {
		validate(request);
		
		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		authenticatedUser = userManager.findById(authenticatedUser.getId());
		
		Friendship friend = null;
		
		List<Friendship> friendList = authenticatedUser.getFriendList();
		if(friendList != null && !friendList.isEmpty()){
			Optional<Friendship> friendship = friendList.stream().filter(x -> x.getUserId().equals(request.getFriendId())).findFirst();
			if(friendship.isPresent()){
				friend = friendship.get(); 
			}
		}
		
		if(friend == null){
			throw new SemNetException(ErrorCode.FRIEND_NOT_FOUND);
		}
		friendList = getDiffFriendList(friendList, friend);
		authenticatedUser.setFriendList(friendList);
		
		userManager.merge(authenticatedUser);
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}
	
	@Override
	public ActionResponse blockFriend(FriendRequest request) {
		validate(request);
		
		User authenticatedUser = userManager.login(request.getAuthToken(), null);
		authenticatedUser = userManager.findById(authenticatedUser.getId());
		
		Friendship friend = null;
		
		List<Friendship> friendList = authenticatedUser.getFriendList();
		if(friendList != null && !friendList.isEmpty()){
			Optional<Friendship> friendship = friendList.stream().filter(x -> x.getUserId().equals(request.getFriendId())).findFirst();
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
		userManager.merge(authenticatedUser);
		
		return new ActionResponse(ErrorCode.SUCCESS);
	}
	
	@Override
	public UserListResponse listFriends(BasicQueryRequest request) {
		validate(request);
		
		User user = userManager.findById(request.getId());
		if(user == null){
			throw new SemNetException(ErrorCode.USER_NOT_FOUND);
		}
		
		UserListResponse response = new UserListResponse(ErrorCode.SUCCESS);
		
		List<Friendship> friendshipList = user.getFriendList();
		if(friendshipList == null || friendshipList.isEmpty()){
			return response;
		}

		
		for (Friendship friendship : friendshipList) {
			
			if(!friendship.isActive()){
				continue;
			}
			
			User friend = userManager.findById(friendship.getUserId());
			
			response.addUser(friend.getId(), friend.getUsername(), friend.getFirstname(), friend.getLastname(), friend.getTagList());
		}
		
		return response;
	}
	
	public List<Friendship> getDiffFriendList(List<Friendship> userList, Friendship user){
		List<Friendship> newList = new ArrayList<>();
		for (Friendship u : userList) {
			if(!u.getId().equals(user.getId())){
				newList.add(u);
			}
		}
		return newList;
	}
}
