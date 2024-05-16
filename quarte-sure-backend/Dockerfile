      # Stage 1: Build app
      FROM maven:3.8.4-openjdk-11 AS build
      WORKDIR /app
      COPY pom.xml .
      COPY src ./src
      RUN mvn clean install

      # Stage 2: Run the app
      FROM openjdk:11
      WORKDIR /app
      COPY --from=build /app/target/quarte-sure-backend-0.0.1-SNAPSHOT.jar ./winner-server.jar
      EXPOSE 8080
      CMD ["java", "-jar", "winner-server.jar"]
