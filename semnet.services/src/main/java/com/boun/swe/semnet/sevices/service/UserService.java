package com.boun.swe.semnet.sevices.service;

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


public interface UserService {

	LoginResponse login(AuthenticationRequest request);
	
	ActionResponse logout(BaseRequest request);
	
	CreateUserResponse create(CreateUserRequest request);

    ActionResponse update(UpdateUserRequest request);

    ActionResponse resetPassword(ResetPasswordRequest request);
    
    ActionResponse changePassword(ChangePasswordRequest request);

    GetUserResponse get(BasicQueryRequest request);
    
    UserListResponse search(BasicSearchRequest request);
    
    ActionResponse addFriend(FriendRequest request);
    ActionResponse removeFriend(FriendRequest request);
    ActionResponse blockFriend(FriendRequest request);
    UserListResponse listFriends(BaseRequest request);
}
