# Stage 1: Build with Java 17 
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy EVERYTHING from your project folder
COPY . .

# Build with error logging enabled (-e) so we can see exact issues
RUN mvn clean package -DskipTests -e

# Stage 2: Run the App
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]