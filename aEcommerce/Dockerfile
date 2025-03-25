# Use an official Java runtime as base image
FROM eclipse-temurin:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the target folder
COPY /target/ecommerce-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on (adjust if needed)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
