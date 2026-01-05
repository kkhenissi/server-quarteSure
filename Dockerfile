# ---------------- Stage 1 : Build Maven ----------------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build

COPY pom.xml .
RUN mvn -B -q dependency:resolve

COPY src ./src
RUN mvn clean package -DskipTests

# ---------------- Stage 2 : Runtime ----------------
FROM eclipse-temurin:17
WORKDIR /app

# Copie du jar généré
COPY --from=build /build/target/quarte-sure-backend-*.jar app.jar

# Railway fournit un PORT dynamique
EXPOSE 8080

# Démarrage de l'application
CMD ["java", "-jar", "app.jar"]
