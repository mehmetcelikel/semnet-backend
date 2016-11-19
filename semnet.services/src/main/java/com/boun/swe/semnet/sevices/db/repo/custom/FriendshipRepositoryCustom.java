package com.boun.swe.semnet.sevices.db.repo.custom;

import java.util.List;

import com.boun.swe.semnet.sevices.db.model.Friendship;

public interface FriendshipRepositoryCustom {

	List<Friendship> findById(String id);

	List<Friendship> merge(Friendship friendship, List<Friendship> friendList);
	
	List<Friendship> delete(Friendship user, List<Friendship> friendList);
}
