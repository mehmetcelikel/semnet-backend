package com.boun.swe.semnet.sevices.service.impl;

import static com.google.common.base.Predicates.in;
import static org.simmetrics.builders.StringMetricBuilder.with;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.CosineSimilarity;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.swe.semnet.commons.constants.AppConstants;
import com.boun.swe.semnet.commons.data.TagData;
import com.boun.swe.semnet.commons.data.request.BasicSearchRequest;
import com.boun.swe.semnet.commons.data.request.TagSearchRequest;
import com.boun.swe.semnet.commons.data.response.QueryLabelResponse;
import com.boun.swe.semnet.commons.dbpedia.OWLClassHierarchy;
import com.boun.swe.semnet.commons.dbpedia.SPARQLRunner;
import com.boun.swe.semnet.commons.exception.SemNetException;
import com.boun.swe.semnet.commons.type.ErrorCode;
import com.boun.swe.semnet.sevices.cache.TagCache;
import com.boun.swe.semnet.sevices.cache.TagCache.TaggedEntityMetaData;
import com.boun.swe.semnet.sevices.data.SemanticSearchResponse;
import com.boun.swe.semnet.sevices.db.model.TaggedEntity;
import com.boun.swe.semnet.sevices.db.model.TaggedEntity.EntityType;
import com.boun.swe.semnet.sevices.service.BaseService;
import com.boun.swe.semnet.sevices.service.SemanticSearchService;
import com.boun.swe.semnet.sevices.service.TagService;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import lombok.Data;


@Service
public class SemanticSearchServiceImpl extends BaseService implements SemanticSearchService{

	@Autowired
	TagService tagService;
	
	@Override
	public QueryLabelResponse queryLabel(BasicSearchRequest request){
		
		validate(request);
		
		return SPARQLRunner.getInstance().runQuery(request.getQueryString());
	}
	
	@Override
	public QueryLabelResponse querySearchString(BasicSearchRequest request) {
		
		validate(request);
		
		List<TagData> tagDataList = TagCache.getInstance(tagService).getAllTags();
		if(tagDataList == null){
			throw new SemNetException(ErrorCode.TAG_NOT_FOUND, "");
		}
		
		QueryLabelResponse response = new QueryLabelResponse(request.getQueryString());
		
		for (TagData tagData : tagDataList) {
			float result = getSimilarityIndex(tagData.getTag(), request.getQueryString());
			
			if(result == 0){
				continue;
			}
			
			response.addData(tagData.getTag(), tagData.getClazz(), null);
		}
		
		return response;
	}
	
	@Override
	public SemanticSearchResponse search(TagSearchRequest request){
		
		SemanticSearchResponse response = new SemanticSearchResponse();
		
		TagData tagData = request.getTagData();
		if(tagData == null){
			throw new SemNetException(ErrorCode.INVALID_INPUT);
		}
	
		List<SemanticSearchIndex> searchIndex = new ArrayList<SemanticSearchIndex>();
		
		List<TagData> tagList = TagCache.getInstance(tagService).getAllTags();
		for (TagData tag : tagList) {
			
			if(tagData.getClazz() != null && tag.getClazz() != null){
				
				if(tag.equals(tagData)){
					searchIndex.add(new SemanticSearchIndex(tag, AppConstants.SEMANTIC_SAME_TAG_FACTOR)); //If both these tags are same, mark it with highest value	
					continue;
				}

				if(tag.getClazz().equalsIgnoreCase(tagData.getClazz())){
					
					float similarityIndex = getSimilarityIndex(tag.getTag(), tagData.getTag());
					searchIndex.add(new SemanticSearchIndex(tag, AppConstants.SEMANTIC_SAME_CONTEXT_FACTOR + similarityIndex)); //If both these tags have same class, mark it with higher value	
					
					continue;
				}
				
				String tagClazzURI = OWLClassHierarchy.getInstance().getClazzURI(tag.getClazz());
				
				String tagDataClazzURI = OWLClassHierarchy.getInstance().getClazzURI(tagData.getClazz());
				float level = getRelationLevel(tagDataClazzURI, tagClazzURI); 
				if(level != 0){
					searchIndex.add(new SemanticSearchIndex(tag, level)); //If both these tags have relation, mark it with high value
				}
				
				continue;
			}
			
			if(tagData.getClazz() == null || "".equalsIgnoreCase(tagData.getClazz())){
				
				String tagDataClazzURI = OWLClassHierarchy.getInstance().getClazzURI(tagData.getTag());
				if(tagDataClazzURI != null){
					//If search string is an actual class, then process it as if it is a context information
					String tagClazzURI = OWLClassHierarchy.getInstance().getClazzURI(tag.getClazz());
					
					float level = getRelationLevel(tagDataClazzURI, tagClazzURI);
					if(level != 0){
						searchIndex.add(new SemanticSearchIndex(tag, level)); //If both these tags have relation, mark it with high value
					}
					
					continue;
				}
				
				//Compare entered text with context information, if they are similar at a degree, consider it as a possible result
				
				float similarityIndex = 0;
				if(tag.getClazz() != null){
					similarityIndex = getSimilarityIndex(tag.getClazz(), tagData.getTag());
					if(similarityIndex > 0.5F){
						searchIndex.add(new SemanticSearchIndex(tag, similarityIndex));
						continue;
					}	
				}
				//If both input tag has no context, compare their similarity 
				similarityIndex = getSimilarityIndex(tag.getTag(), tagData.getTag());
				if(similarityIndex > 0.2F){
					searchIndex.add(new SemanticSearchIndex(tag, similarityIndex));
				}
			}
		}
		
		if(searchIndex.isEmpty()){
			return response;
		}
		
		Collections.sort(searchIndex, new SemanticSearchIndexSort());
		
		for (SemanticSearchIndex index : searchIndex) {
			List<TaggedEntityMetaData> tagEntityIdList = TagCache.getInstance(tagService).getTag(index.getTag());
			
			if(tagEntityIdList == null || tagEntityIdList.isEmpty()){
				continue;
			}
			
			for (TaggedEntityMetaData taggedEntityMetaData : tagEntityIdList) {
				addResultList(response, taggedEntityMetaData.getType(), taggedEntityMetaData.getId(), index.getSimilarityIndex());
			}
		}

		return response;
	}
	
	private void addResultList(SemanticSearchResponse response, EntityType type, String id, float rank){
		
		response.addDetail(type, id, rank, true);
		
		if(rank > AppConstants.SEMANTIC_INDEX_LIMIT){
			TaggedEntity taggedEntity = resolveEntity(type, id);
			
			//If rank is higher than given amount, resolve tag relations of that entity
			for (TagData tagData : taggedEntity.getTagList()) {
				
				List<TaggedEntityMetaData> tagEntityIdList = TagCache.getInstance(tagService).getTag(tagData);
				if(tagEntityIdList == null || tagEntityIdList.isEmpty()){
					continue;
				}
				
				for (TaggedEntityMetaData taggedEntityMetaData : tagEntityIdList) {
					TaggedEntity entity = resolveEntity(taggedEntityMetaData.getType(), taggedEntityMetaData.getId());
					if(entity == null){
						continue;
					}
					
					response.addDetail(entity.getEntityType(), entity.getId(), AppConstants.SEMANTIC_INDEX_LIMIT, false);
				}
			}
		}
	}
	
	public TaggedEntity resolveEntity(EntityType type, String id){
		if(type.equals(EntityType.USER)){
			return userManager.findById(id);
		}
		return contentManager.findById(id);
	}
	
	private float getRelationLevel(String clazz1URI, String clazz2URI){
		int level = OWLClassHierarchy.getInstance().isChild(clazz1URI, clazz2URI, 0);
		if(level == 0){
			level = OWLClassHierarchy.getInstance().isChild(clazz2URI, clazz1URI, 0);	
		}
		 
		if(level == 0){
			return 0;
		}
		
		return (AppConstants.SEMANTIC_CONTEXT_RELATION_FACTOR / level);		
	}
	
	public static float getSimilarityIndex(String str1, String str2){
		
		Set<String> commonWords = Sets.newHashSet("it", "is", "a", "and", "the", "are, i");
		
		StringMetric metric = 
				with(new CosineSimilarity<String>())
				.simplify(Simplifiers.toLowerCase())
				.simplify(Simplifiers.removeNonWord())
				.tokenize(Tokenizers.whitespace())
				.filter(Predicates.not(in(commonWords)))
				.tokenize(Tokenizers.qGram(2))
				.build();
		
		return metric.compare(str1, str2);
	}
	
	@Data
	private static class SemanticSearchIndex{
		private TagData tag;
		private float similarityIndex;
		
		private SemanticSearchIndex(TagData tag, float similarityIndex){
			this.tag = tag;
			this.similarityIndex = similarityIndex;
		}
		
		public boolean equals(Object o){
			if(o == null){
				return false;
			}
			SemanticSearchIndex idx = (SemanticSearchIndex)o;
			
			return (idx.getTag().getTag().equalsIgnoreCase(this.getTag().getTag()));
		}
		
		@Override
		public int hashCode() {
			int code = 7;
			code = 89 * code * this.getTag().getTag().hashCode();
			return code;
		}
	}
	
	private static class SemanticSearchIndexSort implements Comparator<SemanticSearchIndex> {

	    @Override
	    public int compare(SemanticSearchIndex o1, SemanticSearchIndex o2) {
	    	
	    	return (o1.getSimilarityIndex() >= o2.getSimilarityIndex()) ? -1 : 1;
	    }
	}
}
