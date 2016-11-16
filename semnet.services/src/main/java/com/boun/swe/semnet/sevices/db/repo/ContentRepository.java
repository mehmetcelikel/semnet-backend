package com.boun.swe.semnet.sevices.db.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.swe.semnet.sevices.db.model.Content;
import com.boun.swe.semnet.sevices.db.repo.custom.ContentRepositoryCustom;

public interface ContentRepository extends MongoRepository<Content, String>, ContentRepositoryCustom {

}
