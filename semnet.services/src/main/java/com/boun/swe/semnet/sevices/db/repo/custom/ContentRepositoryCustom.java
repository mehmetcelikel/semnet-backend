package com.boun.swe.semnet.sevices.db.repo.custom;

import java.util.List;

import com.boun.swe.semnet.sevices.db.model.Content;

public interface ContentRepositoryCustom {

	Content findById(String contentId);
	
	List<Content> findByUserId(String id);
	
	Content merge(Content content);
	
	void delete(Content content);
	
	List<Content> findLatestContents();
	List<Content> findPopularContents();
}
