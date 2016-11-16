package com.boun.swe.semnet.sevices.db.repo.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.swe.semnet.sevices.db.model.Content;
import com.boun.swe.semnet.sevices.db.repo.custom.ContentRepositoryCustom;

public class ContentRepositoryImpl implements ContentRepositoryCustom {

    @Autowired private MongoTemplate mongoTemplate;

	@Override
	public List<Content> findByUserId(String userId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("owner.$id").is(new ObjectId(userId)));

		return mongoTemplate.find(query, Content.class);
	}
	
	
}
