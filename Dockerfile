FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src/
RUN mvn clean package -DskipTests
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
COPY src/main/resources/db ./liquibase/
COPY resources ./resources
EXPOSE 8080
LABEL authors="bunaev"
ENTRYPOINT ["java", "-jar", "app.jar"]
