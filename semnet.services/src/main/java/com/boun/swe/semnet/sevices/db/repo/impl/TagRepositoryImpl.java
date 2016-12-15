package com.boun.swe.semnet.sevices.db.repo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.swe.semnet.commons.data.TagData;
import com.boun.swe.semnet.sevices.db.model.Tag;
import com.boun.swe.semnet.sevices.db.repo.custom.TagRepositoryCustom;

public class TagRepositoryImpl implements TagRepositoryCustom {

    @Autowired private MongoTemplate mongoTemplate;

	@Override
	public Tag findTag(TagData tagData) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("tag.tag").is(tagData.getTag()));
		
		if(tagData.getClazz() != null && !"".equalsIgnoreCase(tagData.getClazz())){
			query.addCriteria(Criteria.where("tag.clazz").is(tagData.getClazz()));	
		}

		Tag tag = mongoTemplate.findOne(query, Tag.class);
		
		return tag;
	}
}
