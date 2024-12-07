# Use the base Java image
#FROM openjdk:17-jdk-alpine
FROM openjdk:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY build/libs/dyndns-1.0.0.jar /app/dyndns.jar

# Copy the YAML configuration file
COPY config/cloudflare.yaml /app/config/application.yaml

# Define the command to run the application
CMD ["java", "-jar", "dyndns.jar"]
