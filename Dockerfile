# Stage 1: Build Stage
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .

# --- ADD THIS DEBUG LINE ---
RUN ls -laR src/

# Compile the application and skip tests to speed up the build
RUN ./gradlew clean build -x test

# Stage 2: Minimal Runtime Stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]