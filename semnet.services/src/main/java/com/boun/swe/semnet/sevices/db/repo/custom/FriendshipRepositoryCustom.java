package com.boun.swe.semnet.sevices.db.repo.custom;

import java.util.List;

import com.boun.swe.semnet.sevices.db.model.Friendship;

public interface FriendshipRepositoryCustom {

	List<Friendship> findById(String id);

	Friendship findById(String sourceId, String targetId);
}
