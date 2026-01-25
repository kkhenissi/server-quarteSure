package com.fdc.quartesure.quartesurebackend.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:4600",
                "http://winner-front.s3-website.eu-west-3.amazonaws.com",
                "https://winner-front.s3-website.eu-west-3.amazonaws.com",
                "https://d2umkbr9haiwq5.cloudfront.net",
                "http://192.168.1.15:4600"
        ));
0
        0
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS"
        ));

        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
