# Stage 1: Build with Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy EVERYTHING from your project folder
COPY . .

# Build the project
RUN mvn clean package -DskipTests -e

# Stage 2: Run the App with Java 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]
