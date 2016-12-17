package com.boun.swe.semnet.sevices.db.repo;

import java.util.List;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.swe.semnet.sevices.db.model.Content;
import com.boun.swe.semnet.sevices.db.repo.custom.ContentRepositoryCustom;

public interface ContentRepository extends MongoRepository<Content, String>, ContentRepositoryCustom {

	List<Content> findByPositionWithin(Circle c);
	List<Content> findByPositionNear(Point p, Distance d);
}
