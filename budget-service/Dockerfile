# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy root pom and the service's pom
COPY pom.xml .
COPY budget-service/pom.xml budget-service/

# Build dependencies (cached)
RUN mvn dependency:go-offline -pl budget-service -am

# Copy source and build
COPY budget-service/src budget-service/src
RUN mvn clean package -DskipTests -pl budget-service -am

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/budget-service/target/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
