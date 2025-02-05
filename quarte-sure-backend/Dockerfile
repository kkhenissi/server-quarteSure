<<<<<<< HEAD
#      # Stage 1: Build app
#      FROM maven:3.8.4-openjdk-11 AS build
#      WORKDIR /app
#      COPY pom.xml .
#      COPY src ./src
#      RUN mvn clean install

      # Stage 2: Run the app
      FROM openjdk:17
      VOLUME /tmp
      WORKDIR /app
      COPY --from=build /app/target/quarte-sure-backend-0.0.1-SNAPSHOT.jar ./winner-server.jar
      EXPOSE 8085
      CMD ["java", "-jar", "winner-server.jar"]
=======
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
      ARG JAR_VERSION=1.0.5
      VOLUME /tmp
      WORKDIR /app
      COPY --from=build /build/target/quarte-sure-backend-*.jar /app/
#      COPY target/quarte-sure-backend*.jar winner-server.jar
      EXPOSE 8085
      ENV DB_URL=jdbc:postgresql://postgresql:5432/winner_db
      ENV ACTIVE_PROFILE=${PROFILE}
      ENV JAR_VERSION=${APP_VERSION}-SNAPSHOT
      ENV EMAIL_HOSTNAME=missing_host_name
      ENV EMAIL_USER_NAME=missing_user_name
      ENV EMAIL_PASSWORD=missing_password
      CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} quarte-sure-backend-${JAR_VERSION}.jar
#      ENTRYPOINT ["java", "-jar", "winner-server.jar"]
>>>>>>> fb4b9870f9facf66d0c34a5d13995cc800dd023a
