# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy root pom and the service's pom
COPY pom.xml .
COPY notification-service/pom.xml notification-service/

# Build dependencies (cached)
RUN mvn dependency:go-offline -pl notification-service -am

# Copy source and build
COPY notification-service/src notification-service/src
RUN mvn clean package -DskipTests -pl notification-service -am

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/notification-service/target/*.jar app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]
