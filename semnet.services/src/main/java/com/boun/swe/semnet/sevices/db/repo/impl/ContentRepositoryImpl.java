package com.boun.swe.semnet.sevices.db.repo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.swe.semnet.sevices.db.model.Content;
import com.boun.swe.semnet.sevices.db.repo.custom.ContentRepositoryCustom;

public class ContentRepositoryImpl implements ContentRepositoryCustom {

    @Autowired private MongoTemplate mongoTemplate;

    public ContentRepositoryImpl() {
	}
    
    public void geoSpatialIndex(){
    	mongoTemplate.indexOps(Content.class).ensureIndex( new GeospatialIndex("position") );
    }
    
	@Override
	public Content findById(String contentId) {
    	
    	System.out.println("RUNNING ContentRepositoryImpl.findById QUERY");
    	
		return mongoTemplate.findById(contentId, Content.class);
	}

	@Override
	public Content merge(Content content) {
		mongoTemplate.save(content);
		return content;
	}
	
	@Override
	public void remove(Content content) {
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
