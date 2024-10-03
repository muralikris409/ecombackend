package com.example.ecombackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;


@SpringBootApplication
public class EcombackendApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(EcombackendApplication.class, args);
	}

}
