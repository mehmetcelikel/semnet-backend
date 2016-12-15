package com.boun.swe.semnet.sevices.service;

import java.util.List;

import com.boun.swe.semnet.commons.data.TagData;
import com.boun.swe.semnet.sevices.db.model.Tag;
import com.boun.swe.semnet.sevices.db.model.TaggedEntity;
import com.boun.swe.semnet.sevices.db.model.TaggedEntity.EntityType;

public interface TagService{

	public void tag(TagData tag, TaggedEntity baseEntity, boolean add);
	
	public void tag(List<TagData> tagList, TaggedEntity baseEntity, boolean add);
	
	public List<TaggedEntity> findTaggedEntityList(TagData tagStr, EntityType type);
	
	public List<Tag> findAllTagList();
	
}
