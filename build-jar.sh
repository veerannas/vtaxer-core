#!/bin/bash

# Exit immediately if a command fails
set -e

echo "Building Spring Boot application with Maven..."

# Clean and package the application
mvn clean package -DskipTests

# Find the built jar file
JAR_FILE=$(find target -name "*.jar" | grep -v "original" | head -n 1)

echo "Build successful. JAR file created at: $JAR_FILE"
