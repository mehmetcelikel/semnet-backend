package com.boun.swe.semnet.sevices.db.repo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.swe.semnet.sevices.db.model.User;
import com.boun.swe.semnet.sevices.db.repo.custom.UserRepositoryCustom;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class UserRepositoryImpl implements UserRepositoryCustom {

    @Autowired private MongoTemplate mongoTemplate;

	@Override
	public User findByUsername(String username) {
    	
		System.out.println("QUERY UserRepositoryImpl.findByUsername RUNNING");
    	
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username));
		
		return mongoTemplate.findOne(query, User.class);
	}

	@Override
	public User findByOneTimeToken(String oneTimeToken) {
		Query query = new Query();
		query.addCriteria(Criteria.where("oneTimeToken").is(oneTimeToken));
		
		return mongoTemplate.findOne(query, User.class);
	}

	@Override
	public List<User> searchUser(String queryString) {
		
		Pattern regex = Pattern.compile("(?i)"+queryString); 
		
		DBObject clause1 = new BasicDBObject("firstname", regex);  
		DBObject clause2 = new BasicDBObject("lastname", regex);    
		BasicDBList or = new BasicDBList();
		or.add(clause1);
		or.add(clause2);
		DBObject query = new BasicDBObject("$or", or);
		
		DBCollection userCollection = mongoTemplate.getCollection("user_");
		DBCursor cursor = userCollection.find(query);
		
		List<User> userList = new ArrayList<User>();
		while (cursor.hasNext()) {
			BasicDBObject user = (BasicDBObject)cursor.next();
			String firstname = user.getString("firstname");
			String lastname = user.getString("lastname");
			String userId = user.getString("_id");
			String username = user.getString("username");
			
			User u = new User();
			u.setFirstname(firstname);
			u.setId(userId);
			u.setLastname(lastname);
			u.setUsername(username);
			
			userList.add(u);
		}
		cursor.close();
		
		return userList;
	}

	@Override
	public User findById(String id) {
		
		System.out.println("QUERY UserRepositoryImpl.findById RUNNING");
		
		return mongoTemplate.findById(id, User.class);
	}

	@Override
	public User merge(User user) {
		mongoTemplate.save(user);
		return user;
	}
}
