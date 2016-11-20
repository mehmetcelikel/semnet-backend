package com.boun.swe.semnet.sevices.db.repo.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.swe.semnet.sevices.db.model.Content;
import com.boun.swe.semnet.sevices.db.repo.custom.ContentRepositoryCustom;

public class ContentRepositoryImpl implements ContentRepositoryCustom {

    @Autowired private MongoTemplate mongoTemplate;

    @Cacheable(value="contentCache", key="#userId + _useridentifier", unless = "@result == null")
	@Override
	public List<Content> findByUserId(String userId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("owner.$id").is(new ObjectId(userId)));

		return mongoTemplate.find(query, Content.class);
	}
    
    @Cacheable(value="contentCache", key="#contentId", unless = "@result == null")
	@Override
	public Content findById(String contentId) {
		return mongoTemplate.findById(contentId, Content.class);
	}

	@Caching(
	        put = {
	                @CachePut(value = "contentCache", key = "#content.id"),
	                @CachePut(value = "contentCache", key = "#content.owner.id + _useridentifier"),
	        }
	)
	@Override
	public Content merge(Content content) {
		mongoTemplate.save(content);
		return content;
	}
	
	@Caching(
	        evict = {
	        		@CacheEvict(value="contentCache", key="#content.id"),
	        		@CacheEvict(value="contentCache", key="#content.owner.id + _useridentifier")
	        }
	)
	@Override
	public void delete(Content content) {
		mongoTemplate.remove(content);
	}
	
	@Override
	public List<Content> findLatestContents() {
		Query query = new Query();
		query.with(new Sort(Sort.Direction.DESC, "creationDate"));

		return mongoTemplate.find(query, Content.class);
	}
	
	@Override
	public List<Content> findPopularContents() {
		Query query = new Query();
		query.with(new Sort(Sort.Direction.DESC, "likeCount"));

		return mongoTemplate.find(query, Content.class);
	}
}
