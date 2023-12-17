# Build the base image with openjdk:11
FROM openjdk:11-jre-slim
# Fetch the jar from target
ARG JAR_FILE=target/*.jar
# Copy the built JAR file from the previous stage to the container
COPY ${JAR_FILE} /messenger-system.jar
# Set the command to run the application
CMD ["java", "-jar", "/messenger-system.jar"]

