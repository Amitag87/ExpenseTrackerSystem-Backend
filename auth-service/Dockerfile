# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy root pom and the service's pom
COPY pom.xml .
COPY auth-service/pom.xml auth-service/

# Build dependencies (cached)
RUN mvn dependency:go-offline -pl auth-service -am

# Copy source and build
COPY auth-service/src auth-service/src
RUN mvn clean package -DskipTests -pl auth-service -am

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/auth-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
