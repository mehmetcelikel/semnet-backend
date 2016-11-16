package com.boun.swe.semnet.sevices.db.repo.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.swe.semnet.sevices.db.model.Friendship;
import com.boun.swe.semnet.sevices.db.repo.custom.FriendshipRepositoryCustom;

public class FriendshipRepositoryImpl implements FriendshipRepositoryCustom {

    @Autowired private MongoTemplate mongoTemplate;

	@Override
	public List<Friendship> findById(String sourceId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("source.$id").is(new ObjectId(sourceId)));

		return mongoTemplate.find(query, Friendship.class);
	}

	@Override
	public Friendship findById(String sourceId, String targetId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("source.$id").is(new ObjectId(sourceId)).and("target.$id").is(new ObjectId(targetId)));

		return mongoTemplate.findOne(query, Friendship.class);
	}
}
