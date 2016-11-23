package com.boun.swe.semnet.sevices.db.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.boun.swe.semnet.sevices.db.manager.ContentManager;
import com.boun.swe.semnet.sevices.db.model.Content;
import com.boun.swe.semnet.sevices.db.repo.ContentRepository;

@Service
public class ContentManagerImpl implements ContentManager{

	@Autowired
	private ContentRepository contentRepository;

	@Cacheable(value = "contentCache", key = "#contentId", unless = "#result == null")
	@Override
	public Content findById(String contentId) {
		return contentRepository.findById(contentId);
	}

	@Caching(
			put = { 
					@CachePut(value = "contentCache", key = "#content.id"),
			}
	)
	@Override
	public Content merge(Content content) {
		contentRepository.merge(content);
		return content;
	}

	@Caching(
			evict = { 
					@CacheEvict(value = "contentCache", key = "#content.id"),
			}
	)
	@Override
	public void delete(Content content) {
		contentRepository.delete(content);
	}

	@Override
	public List<Content> findLatestContents() {
		return contentRepository.findLatestContents();
	}

	@Override
	public List<Content> findPopularContents() {
		return contentRepository.findPopularContents();
	}
}