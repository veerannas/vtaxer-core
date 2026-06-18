# Use the official OpenJDK 21 image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot jar file into the container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose the application port (optional, but good practice)
EXPOSE 8080

# Set the default command to run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
