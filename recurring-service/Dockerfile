# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy root pom and the service's pom
COPY pom.xml .
COPY recurring-service/pom.xml recurring-service/

# Build dependencies (cached)
RUN mvn dependency:go-offline -pl recurring-service -am

# Copy source and build
COPY recurring-service/src recurring-service/src
RUN mvn clean package -DskipTests -pl recurring-service -am

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/recurring-service/target/*.jar app.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]
