package com.boun.swe.semnet.commons.util;

import static com.google.common.base.Predicates.in;
import static org.simmetrics.builders.StringMetricBuilder.with;

import java.util.Set;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.CosineSimilarity;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizers;

import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

public class SemNetUtils {

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
}
