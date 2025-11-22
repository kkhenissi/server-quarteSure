package com.fdc.quartesure.quartesurebackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class CorsConfig {
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
////                registry.addMapping("/**")
////                        .allowedOrigins("http://winner-one-frontend.s3-website.eu-west-3.amazonaws.com")
////                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
////                        .allowedHeaders("*")
////                        .allowCredentials(true);
//
//
//
//                        registry.addMapping("/**")
//                        .allowedOriginPatterns("http://winner-one-frontend.s3-website.eu-west-3.amazonaws.com")
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
//                        .allowCredentials(true)
//                        .allowedHeaders("*")
//                        .exposedHeaders("Access-Control-Allow-Origin");
//
//            }
//        };
//    }
//}


//@Configuration
public class CorsConfig {

 //   @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // toutes les routes
                        .allowedOrigins("http://winner-one-frontend.s3-website.eu-west-3.amazonaws.com") // ton frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization", "Content-Type")
                        .allowCredentials(true);
            }
        };
    }
}