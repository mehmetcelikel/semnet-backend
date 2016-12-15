package com.boun.swe.semnet.sevices.db.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.swe.semnet.sevices.db.model.Tag;
import com.boun.swe.semnet.sevices.db.repo.custom.TagRepositoryCustom;

public interface TagRepository extends MongoRepository<Tag, String>, TagRepositoryCustom {
;
}
