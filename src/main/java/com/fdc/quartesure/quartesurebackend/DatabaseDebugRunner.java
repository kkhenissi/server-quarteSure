package com.fdc.quartesure.quartesurebackend;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseDebugRunner implements CommandLineRunner {

    private final HikariDataSource hikariDataSource;

    public DatabaseDebugRunner(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1️⃣ Lire directement la variable d'environnement
        String databaseUrlEnv = System.getenv("DATABASE_URL");
        if (databaseUrlEnv == null) {
            System.out.println("[DEBUG] DATABASE_URL is NULL! Spring Boot cannot resolve it.");
        } else {
            System.out.println("[DEBUG] DATABASE_URL environment variable = " + databaseUrlEnv);
        }

        // 2️⃣ Lire l'URL JDBC que Hikari tente d'utiliser
        try {
            String jdbcUrl = hikariDataSource.getJdbcUrl();
            System.out.println("[DEBUG] Hikari JDBC URL = " + jdbcUrl);
        } catch (Exception e) {
            System.out.println("[DEBUG] Hikari JDBC URL cannot be read yet: " + e.getMessage());
        }

        // 3️⃣ Vérifier si Hikari peut obtenir une connexion
        try {
            hikariDataSource.getConnection().close();
            System.out.println("[DEBUG] Successfully connected to the database via Hikari!");
        } catch (Exception e) {
            System.out.println("[DEBUG] Cannot connect to the database: " + e.getMessage());
        }
    }
}
