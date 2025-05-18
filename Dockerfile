FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src/
RUN mvn clean package -DskipTests
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
LABEL authors="bunaev"
CMD ["sh", "-c", "sleep 30 && java -jar app.jar"]
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--logging.level.liquibase=DEBUG", "--logging.level.org.springframework=DEBUG"]
