package com.boun.swe.semnet.sevices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.boun.swe*")
public class SemNetServicesApplication {
	
	public static void main(String[] args) {
        SpringApplication.run(SemNetServicesApplication.class, args);
    }
}
