package com.boun.swe.semnet.sevices.db.repo.impl;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.swe.semnet.sevices.db.model.Friendship;
import com.boun.swe.semnet.sevices.db.repo.custom.FriendshipRepositoryCustom;

public class FriendshipRepositoryImpl implements FriendshipRepositoryCustom {

    @Autowired private MongoTemplate mongoTemplate;

    @Cacheable(value="friendCache", key="#userId")
	@Override
	public List<Friendship> findById(String userId) {
		
		System.out.println("Running friendlist query");
		
		Query query = new Query();
		query.addCriteria(Criteria.where("source.$id").is(new ObjectId(userId)));

		return mongoTemplate.find(query, Friendship.class);
	}
	
    @Caching(
	        put = {
	                @CachePut(value = "friendCache", key = "#user.source.id"),
	        }
	)
	@Override
	public List<Friendship> merge(Friendship user, List<Friendship> friendList) {
    	
		mongoTemplate.save(user);
		
		if(friendList == null){
			friendList = new ArrayList<>();
		}
		friendList.add(user);
		
		return friendList;
	}

    @Caching(
	        put = {
	                @CachePut(value = "friendCache", key = "#user.source.id"),
	        }
	)
	@Override
	public List<Friendship> delete(Friendship user, List<Friendship> friendList) {
    	
		mongoTemplate.remove(user);
		
		if(friendList != null && friendList.contains(user)){
			friendList.remove(user);
		}
		
		return friendList;
	}
}
