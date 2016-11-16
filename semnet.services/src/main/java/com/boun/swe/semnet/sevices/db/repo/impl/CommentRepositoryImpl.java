package com.boun.swe.semnet.sevices.db.repo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.boun.swe.semnet.sevices.db.repo.custom.CommentRepositoryCustom;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    @Autowired private MongoTemplate mongoTemplate;

	
	
	
}
