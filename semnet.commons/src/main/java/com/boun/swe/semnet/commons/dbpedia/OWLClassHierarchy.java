package com.boun.swe.semnet.commons.dbpedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

import lombok.Data;

public class OWLClassHierarchy {

	private static OWLClassHierarchy instance = new OWLClassHierarchy();
	
	private Hashtable<String, Node> hierarchy = new Hashtable<String, Node>();
	
	private Hashtable<String,String> classURITable = new Hashtable<String,String>();
	
	private OWLClassHierarchy() {
		try{
			initializeOWL();
			initializeYAGO();
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	public static OWLClassHierarchy getInstance() {
		return instance;
	}
	
	public Hashtable<String, Node> getHierarchy() {
		return hierarchy;
	}
	
	public String getClazzURI(String clazz){
		if(clazz == null){
			return null;
		}
		return classURITable.get(clazz);
	}
	
	@Data
	public static class Node{
		private String label;
		private String uri;
		private Element parent;
		private List<Element> childList = new ArrayList<Element>();
	}
	
	@Data
	public static class Element{
		private String label;
		private String uri;
		
		private Element(String label, String uri){
			this.label = label;
			this.uri = uri;
		}
		
		public boolean equals(Object o){
			if(o == null){
				return false;
			}
			Element el = (Element)o;
			
			return (el.getUri().equalsIgnoreCase(this.getUri()));
		}
		
		@Override
		public int hashCode() {
			int code = 7;
			code = 13 * code * this.getUri().hashCode();
			return code;
		}
	}
	
	private void initializeOWL() throws IOException{
		
		final Model dbpedia = ModelFactory.createDefaultModel();

		String os = System.getProperty("os.name");
		
		InputStream in = null;
		if(os.contains("Windows")){
			in = new FileInputStream(new File("C:/Users/mehmetce/git/semnet_backend/semnet.commons/resources/dbpedia_2016-04.owl"));	
		}else{
			in = new FileInputStream(new File("/swe599/resources/dbpedia_2015-04.owl"));
		}
		
        
        dbpedia.read(in, "RDF/XML" );
        final StmtIterator stmts = dbpedia.listStatements(null, RDFS.subClassOf, (RDFNode) null);
        while ( stmts.hasNext() ) {
            final Statement stmt = stmts.next();
            
            String clazzUri = stmt.getSubject().getURI();
            String clazz = stmt.getSubject().getLocalName();
            String parentClazz = stmt.getObject().asResource().getLocalName();
            String parentClazzUri = stmt.getObject().asResource().getURI();
            
            processParent(parentClazz, parentClazzUri, clazz, clazzUri);
            processChild(parentClazz, parentClazzUri, clazz, clazzUri);
            
            classURITable.put(clazz, clazzUri);
            classURITable.put(parentClazz, parentClazzUri);
        }
	}
	
	private void initializeYAGO() {

		String localFile = "C:/Users/mehmetce/git/semnet_backend/semnet.commons/resources/yago_taxonomy.nt";
		String remoteFile = "/swe599/resources/yago_taxonomy.nt";
		
		final OntModel dbpedia = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

		String os = System.getProperty("os.name");
		
		String file = null;
		if(os.contains("Windows")){
			file = localFile;	
		}else{
			file = remoteFile;
		}
		
        dbpedia.read(file, "" );
        final StmtIterator stmts = dbpedia.listStatements(null, RDFS.subClassOf, (RDFNode) null);
        while ( stmts.hasNext() ) {
            final Statement stmt = stmts.next();
            
            String clazzUri = stmt.getSubject().getURI();
            String clazz = stmt.getSubject().getLocalName();
            String parentClazz = stmt.getObject().asResource().getLocalName();
            String parentClazzUri = stmt.getObject().asResource().getURI();

            processParent(parentClazz, parentClazzUri, clazz, clazzUri);
            processChild(parentClazz, parentClazzUri, clazz, clazzUri);
            
            classURITable.put(clazz, clazzUri);
            classURITable.put(parentClazz, parentClazzUri);
        }
	}
	
	private void processParent(String parentClazz, String parentClazzUri, String childClazz, String clazzUri){
		
		 Node parentNode = hierarchy.get(parentClazzUri);
         if(parentNode == null){
         	parentNode = new Node();
         	parentNode.setLabel(parentClazz);
         	parentNode.setUri(parentClazzUri);
         	parentNode.setParent(new Element(parentClazz, parentClazzUri));
         }
         parentNode.getChildList().add(new Element(childClazz, clazzUri));
         hierarchy.put(parentClazzUri, parentNode);
	}
	
	private void processChild(String parentClazz, String parentClazzUri, String childClazz, String childClazzUri){
		
		Node node = hierarchy.get(childClazzUri);
        if(node == null){
        	node = new Node();
        	node.setLabel(childClazz);
        	node.setUri(childClazzUri);
        	node.setParent(new Element(parentClazz, parentClazzUri));
        }
        node.setParent(new Element(parentClazz, parentClazzUri));
        hierarchy.put(childClazzUri, node);
	}
	
	public int isChild(String clazz1URI, String clazz2URI, int level){
		 
		if(clazz1URI == null || clazz2URI == null){
			return level;
		}
		
		Node node1 = getInstance().getHierarchy().get(clazz1URI);
		Node node2 = getInstance().getHierarchy().get(clazz2URI);
		
		if(node1 == null || node2 == null){
			return level;
		}

		++level;

		if(clazz1URI.equalsIgnoreCase(clazz2URI)){
			return level;
		}
		
		if(node1.getParent().getLabel().equalsIgnoreCase(node2.getLabel())){
			return level;
		}
		
		for (Element element : node2.getChildList()) {
			if(node1.getLabel().equalsIgnoreCase(element.getLabel())){
				return level;
			}
		}
		
		for (Element element : node2.getChildList()) {
			if(isChild(clazz1URI, element.getUri(), ++level) != 0){
				return level;
			}
		}
		
		return 0;
	}
}
