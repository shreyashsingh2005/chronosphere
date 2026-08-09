# Stage 1: Build with Java 21 (Limited RAM to prevent Render crash)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY . .

# Restrict Maven memory usage
ENV MAVEN_OPTS="-Xmx256m"
RUN mvn clean package -DskipTests -e

# Stage 2: Run the App
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]