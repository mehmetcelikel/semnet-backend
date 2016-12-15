package com.boun.swe.semnet.sevices.cache;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.boun.swe.semnet.commons.data.TagData;
import com.boun.swe.semnet.sevices.db.model.Tag;
import com.boun.swe.semnet.sevices.db.model.TaggedEntity;
import com.boun.swe.semnet.sevices.service.TagService;

import lombok.Data;

public class TagCache {

	private static TagCache instance = null;
	
	private Hashtable<TagData, List<TaggedEntityMetaData>> tagTable = new Hashtable<TagData, List<TaggedEntityMetaData>>();
	
	private TagCache(){
	}
	
	public static synchronized TagCache getInstance(TagService tagService) {
		if(instance == null){
			instance = new TagCache();
			instance.buildCache(tagService);
		}
		return instance;
	}
	
	private void buildCache(TagService tagService){
		List<Tag> tagList = tagService.findAllTagList();
		
		for (Tag tag : tagList) {
			
			List<TaggedEntityMetaData> list = tagTable.get(tag.getTag());
			if(list == null){
				list = new ArrayList<TaggedEntityMetaData>();
			}
			for (TaggedEntity entity : tag.getReferenceSet()) {
				list.add(new TaggedEntityMetaData(entity.getId(), entity.getEntityType()));
			}
			tagTable.put(tag.getTag(), list);
		}
	}
	
	public List<TaggedEntityMetaData> getTag(TagData tag){
		return tagTable.get(tag);
	}
	
	public List<TagData> getAllTags(){
		
		if(tagTable.isEmpty()){
			return null;
		}
		
		return new ArrayList<TagData>(tagTable.keySet());
	}

	public void updateTag(TagData tag, List<TaggedEntity> tagList){
		List<TaggedEntityMetaData> list = new ArrayList<TaggedEntityMetaData>();
		for (TaggedEntity taggedEntity : tagList) {
			list.add(new TaggedEntityMetaData(taggedEntity.getId(), taggedEntity.getEntityType()));
		}
		tagTable.put(tag, list);
	}
	
	@Data
	public static class TaggedEntityMetaData{
		private String id;
		private TaggedEntity.EntityType type;
		
		private TaggedEntityMetaData(String id, TaggedEntity.EntityType type){
			this.id = id;
			this.type = type;
		}
	}
}