package com.boun.swe.semnet.sevices.db.repo.custom;

import com.boun.swe.semnet.commons.data.TagData;
import com.boun.swe.semnet.sevices.db.model.Tag;

public interface TagRepositoryCustom {

	public Tag findTag(TagData tagData);
}
