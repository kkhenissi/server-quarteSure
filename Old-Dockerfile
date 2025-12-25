# Stage 1 : Build app
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests


# Stage 2 : Runtime
FROM eclipse-temurin:17
ARG PROFILE=dev
ARG JAR_VERSION=1.0.6

VOLUME /tmp
WORKDIR /app

# Copie du jar généré
COPY --from=build /build/target/quarte-sure-backend-*.jar /app/

EXPOSE 8085

# Variables d'environnement
ENV DB_URL=jdbc:postgresql://postgres:5432/winner_db
ENV ACTIVE_PROFILE=${PROFILE}
ENV APP_VERSION=${JAR_VERSION}-SNAPSHOT
ENV EMAIL_HOSTNAME=missing_host_name
ENV EMAIL_USER_NAME=missing_user_name
ENV EMAIL_PASSWORD=missing_password

# Démarrage de l'application
CMD java -jar \
    -Dspring.profiles.active=${ACTIVE_PROFILE} \
    -Dspring.datasource.url=${DB_URL} \
    quarte-sure-backend-${APP_VERSION}.jar
