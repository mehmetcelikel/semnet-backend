package com.boun.swe.semnet.sevices.service;

import com.boun.swe.semnet.commons.data.request.BasicQueryRequest;
import com.boun.swe.semnet.commons.data.request.FriendRequest;
import com.boun.swe.semnet.commons.data.response.ActionResponse;
import com.boun.swe.semnet.commons.data.response.UserListResponse;

public interface FriendService {

    ActionResponse addFriend(FriendRequest request);
    ActionResponse removeFriend(FriendRequest request);
    ActionResponse blockFriend(FriendRequest request);
    UserListResponse listFriends(BasicQueryRequest request);
}
