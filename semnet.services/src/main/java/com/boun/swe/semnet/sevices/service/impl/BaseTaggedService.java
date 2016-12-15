package com.boun.swe.semnet.sevices.service.impl;

import java.util.List;

import com.boun.swe.semnet.commons.data.TagData;
import com.boun.swe.semnet.commons.data.request.TagRequest;
import com.boun.swe.semnet.commons.data.response.ActionResponse;
import com.boun.swe.semnet.commons.type.ErrorCode;
import com.boun.swe.semnet.sevices.db.model.TaggedEntity;
import com.boun.swe.semnet.sevices.service.BaseService;
import com.boun.swe.semnet.sevices.service.TagService;

public abstract class BaseTaggedService extends BaseService{

    protected abstract TagService getTagService();

    public abstract TaggedEntity findById(String entityId);
    
    public abstract void save(TaggedEntity entity);
    
	public ActionResponse tag(TagRequest request) {
		
		validate(request);
		
		TaggedEntity taggedEntity = findById(request.getEntityId());
		
		ActionResponse response = new ActionResponse(ErrorCode.SUCCESS);

		List<TagData> tagList = request.getTag();
		if(tagList == null || tagList.isEmpty()){
			return response;
		}
		
		for (TagData tagData : tagList) {
			if(taggedEntity.updateTagList(tagData, request.isAdd())){
				getTagService().tag(request.getTag(), taggedEntity, request.isAdd());
			
				save(taggedEntity);
			}	
		}
		
		return response;
	}
	
	public void updateTag(TaggedEntity taggedEntity, List<TagData> newList) {
		
		List<TagData> addedTagList = taggedEntity.addTagList(newList);
		if(addedTagList != null && !addedTagList.isEmpty()){
			for (TagData tag : addedTagList) {
				getTagService().tag(tag, taggedEntity, true);
			}	
		}

		List<TagData> removedTagList = taggedEntity.removeTagList(newList);
		if(removedTagList != null && !removedTagList.isEmpty()){
			for (TagData tag : removedTagList) {
				getTagService().tag(tag, taggedEntity, false);
			}	
		}
	}
}
