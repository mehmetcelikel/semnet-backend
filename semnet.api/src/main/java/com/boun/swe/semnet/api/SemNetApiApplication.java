package com.boun.swe.semnet.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.boun.swe.semnet.commons.dbpedia.OWLClassHierarchy;
import com.boun.swe.semnet.sevices.db.repo.ContentRepository;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class SemNetApiApplication extends SpringBootServletInitializer {
    public static void main( String[] args ){
    	OWLClassHierarchy.getInstance();
    	
    	ConfigurableApplicationContext context = SpringApplication.run(SemNetApiApplication.class, args);

        context.getBean(ContentRepository.class).geoSpatialIndex();
    }
}
