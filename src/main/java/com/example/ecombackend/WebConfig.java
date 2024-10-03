package com.example.ecombackend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all endpoints
                .allowedOrigins("http://127.0.0.1:5500") // Replace with your frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed HTTP methods
                .allowCredentials(true);
        registry.addMapping("/**")  // Allow CORS for all endpoints
        .allowedOrigins("http://localhost:3000")  // Allow the frontend origin
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow these HTTP methods
        .allowedHeaders("*")  // Allow all headers
        .allowCredentials(true)  // If your API supports credentials
        .maxAge(3600);  
        
    }
}
