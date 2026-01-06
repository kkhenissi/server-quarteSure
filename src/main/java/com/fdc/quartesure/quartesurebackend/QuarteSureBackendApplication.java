package com.fdc.quartesure.quartesurebackend;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@SpringBootApplication
@EnableAsync
public class QuarteSureBackendApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(QuarteSureBackendApplication.class, args);
/*
		double learningRate=0.001;
		int numbInputs=7;
		int numbHidden=10;
		int numbOutputs=5;
		int batchSize=1;
		int classIndex=7;
        int numEpochs=500;
*/










	}
	@Configuration
	public class CorsConfig {
		@Bean
		public WebMvcConfigurer corsConfigurer() {
			return new WebMvcConfigurer() {
				@Override
				public void addCorsMappings(CorsRegistry registry) {
					registry.addMapping("/**")
							.allowedOrigins(
									"https://localhost:4600",
									"https://winner-one-frontend.s3-website.eu-west-3.amazonaws.com"
							)
							.allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
							.allowedHeaders("*")
							.allowCredentials(true);
				}
			};
		}
	}
}
