      # Stage 1: Build app
      FROM maven:3.8.7-openjdk-18 AS build
      WORKDIR /build
      COPY pom.xml .
      RUN mvn dependency:go-offline
      COPY src ./src
      RUN mvn clean package -DskipTests

      # Runtime stage
      FROM openjdk:17
      ARG PROFILE=dev
      ARG APP_VERSION=1.0.0
      VOLUME /tmp
      WORKDIR /app
      COPY --from=build /build/target/quarte-sure-backend-*.jar /app/
#      COPY target/quarte-sure-backend*.jar winner-server.jar
      EXPOSE 8085
      ENV DB_URL=jdbc:postgresql://postgresql:5432/winner_db
      ENV ACTIVE_PROFILE=PROFILE
      ENV JAR_VERSION=${APP_VERSION}-SNAPSHOT
      CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} quarte-sure-backend-${JAR_VERSION}.jar
#      ENTRYPOINT ["java", "-jar", "winner-server.jar"]
