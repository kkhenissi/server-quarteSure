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

		System.out.println("=== ENV CHECK (before Spring) ===");
		System.out.println("PGHOST=" + System.getenv("PGHOST"));
		System.out.println("PGPORT=" + System.getenv("PGPORT"));
		System.out.println("PGDATABASE=" + System.getenv("PGDATABASE"));
		System.out.println("PGUSER=" + System.getenv("PGUSER"));
		System.out.println("PGPASSWORD=" + System.getenv("PGPASSWORD"));
		System.out.println("========== START VERSION 2026-07-15 ==========");

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
									"http://winner-front.s3-website.eu-west-3.amazonaws.com/"
							)
							.allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
							.allowedHeaders("*")
							.allowCredentials(true);
				}
			};
		}
	}

}
