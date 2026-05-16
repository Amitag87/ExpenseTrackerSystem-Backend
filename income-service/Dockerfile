# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy root pom and the service's pom
COPY pom.xml .
COPY income-service/pom.xml income-service/

# Build dependencies (cached)
RUN mvn dependency:go-offline -pl income-service -am

# Copy source and build
COPY income-service/src income-service/src
RUN mvn clean package -DskipTests -pl income-service -am

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/income-service/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
