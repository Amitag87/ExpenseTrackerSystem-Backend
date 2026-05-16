# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy root pom and the service's pom
COPY pom.xml .
COPY analytics-service/pom.xml analytics-service/

# Build dependencies (cached)
RUN mvn dependency:go-offline -pl analytics-service -am

# Copy source and build
COPY analytics-service/src analytics-service/src
RUN mvn clean package -DskipTests -pl analytics-service -am

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/analytics-service/target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
