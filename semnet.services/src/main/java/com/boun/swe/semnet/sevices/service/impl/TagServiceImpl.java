package com.boun.swe.semnet.sevices.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.swe.semnet.commons.data.TagData;
import com.boun.swe.semnet.commons.dbpedia.OWLClassHierarchy;
import com.boun.swe.semnet.commons.exception.SemNetException;
import com.boun.swe.semnet.commons.type.ErrorCode;
import com.boun.swe.semnet.sevices.cache.TagCache;
import com.boun.swe.semnet.sevices.db.model.Tag;
import com.boun.swe.semnet.sevices.db.model.TaggedEntity;
import com.boun.swe.semnet.sevices.db.model.TaggedEntity.EntityType;
import com.boun.swe.semnet.sevices.db.repo.TagRepository;
import com.boun.swe.semnet.sevices.service.BaseService;
import com.boun.swe.semnet.sevices.service.TagService;

@Service
public class TagServiceImpl extends BaseService implements TagService{

	@Autowired
	private TagRepository tagRepository;
	
	@Override
	public void tag(TagData tagStr, TaggedEntity baseEntity, boolean add) {
		
		if(tagStr == null){
			return;
		}
		
		if(tagStr.getClazz() != null && !"".contentEquals(tagStr.getClazz())){
			
			String clazzURI = OWLClassHierarchy.getInstance().getClazzURI(tagStr.getClazz());
			
			if(clazzURI == null){
				throw new SemNetException(ErrorCode.TAG_CLASS_NOT_FOUND);
			}	
		}
		
		Tag tag = tagRepository.findTag(tagStr);
		if(tag == null){
			
			if(!add){
				return;
			}
			
			tag = new Tag();
			tag.setTag(tagStr);
		}
		
		List<TaggedEntity> referenceSet = tag.getReferenceSet();
		if(referenceSet == null || referenceSet.isEmpty()){
			
			if(!add){
				return;
			}
			
			referenceSet = new ArrayList<TaggedEntity>();
		}
		
		if(add){
		
			boolean found = false;
			for (TaggedEntity taggedEntity : referenceSet) {
				if(taggedEntity.getId().equalsIgnoreCase(baseEntity.getId())){
					found = true;
					break;
				}
			}
			if(!found){
				referenceSet.add(baseEntity);
			}
			
		}else{

			// remove baseEntity from list
			List<TaggedEntity> newSet = new ArrayList<TaggedEntity>();
			for (TaggedEntity taggedEntity : referenceSet) {
				if(!taggedEntity.getId().equalsIgnoreCase(baseEntity.getId())){
					newSet.add(taggedEntity);
				}
			}
			referenceSet.clear();
			referenceSet.addAll(newSet);
		}
		
		tag.setReferenceSet(referenceSet);
		
		TagCache.getInstance(this).updateTag(tag.getTag(), tag.getReferenceSet());
		
		tagRepository.save(tag);
	}

	@Override
	public void tag(List<TagData> tagList, TaggedEntity baseEntity, boolean add) {
		if(tagList == null || tagList.isEmpty()){
			return;
		}
		for (TagData tag : tagList) {
			tag(tag, baseEntity, add);
		}
	}
	
	public List<TaggedEntity> findTaggedEntityList(TagData tagStr, EntityType type){
		Tag tag = tagRepository.findTag(tagStr);
		
		if(tag == null){
			return null;
		}
		
		List<TaggedEntity> resultList = new ArrayList<TaggedEntity>();
		for (TaggedEntity entity : tag.getReferenceSet()) {
			if(entity.getEntityType() == type){
				resultList.add(entity);
			}
		}
		return resultList;
	}

	@Override
	public List<Tag> findAllTagList() {
		return tagRepository.findAll();
	}

}
