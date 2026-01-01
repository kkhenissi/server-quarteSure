package com.fdc.quartesure.quartesurebackend;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatasourceDebug {

    // Injection de la variable d'environnement
    @Value("${DATABASE_URL:#{null}}")
    private String databaseUrl;

    @PostConstruct
    public void logDatabaseUrl() {
        if (databaseUrl == null) {
            System.out.println("[DEBUG] DATABASE_URL is NULL! Spring Boot cannot resolve it.");
        } else {
            System.out.println("[DEBUG] DATABASE_URL = " + databaseUrl);
        }
    }
}
