package com.boun.swe.semnet.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.boun.swe.semnet.commons.dbpedia.OWLClassHierarchy;

@SpringBootApplication
public class SemNetApiApplication 
{
    public static void main( String[] args ){
    	OWLClassHierarchy.getInstance();
    	
    	SpringApplication.run(SemNetApiApplication.class, args);
    }
}
