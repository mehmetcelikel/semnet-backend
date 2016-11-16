package com.boun.swe.semnet.sevices.db.repo.custom;

import java.util.List;

import com.boun.swe.semnet.sevices.db.model.Content;

public interface ContentRepositoryCustom {

	List<Content> findByUserId(String id);
}
