# Stage 1: Build all services
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the root pom.xml and all service directories
COPY pom.xml .
COPY analytics-service/ analytics-service/
COPY auth-service/ auth-service/
COPY budget-service/ budget-service/
COPY category-service/ category-service/
COPY expense-service/ expense-service/
COPY income-service/ income-service/
COPY notification-service/ notification-service/
COPY recurring-service/ recurring-service/

# Build all modules
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# The SERVICE_NAME argument specifies which service to run
# This should be set in Render's Build Arguments
ARG SERVICE_NAME
ENV SERVICE_NAME=${SERVICE_NAME}

# Copy the specific JAR from the build stage
# Note: Using a wildcard for the version to be flexible
COPY --from=build /app/${SERVICE_NAME}/target/*.jar app.jar

# Standard Spring Boot port (individual services might have different ports, but Render expects 8080 or the PORT env var)
# We can use the PORT environment variable provided by Render
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
