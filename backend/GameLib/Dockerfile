# Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Código
COPY src ./src
RUN mvn clean package -DskipTests

# Imagem
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# User não root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 9000

# Config de memória para hospedagem
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]