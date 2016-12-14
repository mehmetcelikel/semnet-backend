package com.boun.swe.semnet.commons.dbpedia;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boun.swe.semnet.commons.cache.DBPediaCache;
import com.boun.swe.semnet.commons.data.response.QueryLabelResponse;
import com.boun.swe.semnet.commons.dbpedia.OWLClassHierarchy.Node;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class SPARQLRunner {

	private final static Logger logger = LoggerFactory.getLogger(SPARQLRunner.class);
	
	private static SPARQLRunner instance = new SPARQLRunner();
	
	private String DBPEDIA_QUERY ="PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
									"PREFIX  rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n" +
									"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>\n\n" +
										"select ?label ?type ?ctgry where {\n" +
										"?s rdfs:label ?label.\n" +
										"?s rdf:type ?type.\n" +
										"?s rdfs:subClassOf* ?ctgry.\n" +
										"FILTER langMatches( lang(?label), 'EN' ).\n"+
										"?label <bif:contains> \"'%s'\" .\n" +
									"} LIMIT 1000";
	
	
	private SPARQLRunner(){
	}
	
	public static SPARQLRunner getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		QueryLabelResponse data = getInstance().runQuery("Fenerbahçe S.K.");
		System.out.println(data);
	}
	
	public QueryLabelResponse runQuery(String queryString) {

		QueryLabelResponse response = DBPediaCache.getInstance().get(queryString);
		if(response != null){
			return response;
		}
		
		response = new QueryLabelResponse(queryString);
		
		String dbpeaidSparqlQueryString = String.format(DBPEDIA_QUERY, queryString);
		
        Query query = QueryFactory.create(dbpeaidSparqlQueryString);
        QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
        ResultSet results = qExe.execSelect();
        
        Hashtable<String, List<String>> resultTable = new Hashtable<String, List<String>>();
        
        while(results.hasNext()){
        	QuerySolution qs = results.next();
        	
        	String label = qs.get("label").asLiteral().getString();
        	String type = qs.get("type").toString();
        	String ctgry = qs.get("ctgry").toString();
        	
        	
        	Node node = OWLClassHierarchy.getInstance().getHierarchy().get(type);
        	if(node == null){
        		node = OWLClassHierarchy.getInstance().getHierarchy().get(ctgry);
        	}
        	
        	if(node == null){
        		continue;
        	}
        	
        	List<String> typeList = resultTable.get(label);
        	if(typeList == null){
        		typeList = new ArrayList<String>();
        	}
        	typeList.add(node.getUri());
        	
        	resultTable.put(label, typeList);
        }
        
        for (String label : resultTable.keySet()) {
        	Node current = null;
        	
        	List<String> typeList = resultTable.get(label);
        	for (String type : typeList) {
				
        		if(type.equalsIgnoreCase("http://www.w3.org/2002/07/owl#Thing")){
        			continue;
        		}
        		
        		Node node = OWLClassHierarchy.getInstance().getHierarchy().get(type);
            	if(node == null){
            		continue;
            	}
            	
            	if(current == null){
            		current = node;
            		continue;
            	}
            	
            	int level = OWLClassHierarchy.getInstance().isChild(node.getUri(), current.getUri(), 0);
            	if(level != 0){
            		current = node;
            	}
            	
            	if(current != null){
    	        	response.addData(label, current.getLabel(), null);
    	        }
			}
        	
        	//TODO load umbel, wikidata and skos classes
		}
        
        logger.info("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRr5");
        
        DBPediaCache.getInstance().put(queryString, response);
        
        return response;
    }
}