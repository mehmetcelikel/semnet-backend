package com.boun.swe.semnet.sevices.db.manager;

import java.util.List;

import com.boun.swe.semnet.sevices.db.model.Content;

public interface ContentManager {

	public Content findById(String contentId);

	public Content merge(Content content);

	public void delete(Content content);

	public List<Content> findLatestContents();

	public List<Content> findPopularContents();
}
