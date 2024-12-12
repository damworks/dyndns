#!/bin/bash

# Image and container name
IMAGE_NAME="dyndns"
CONTAINER_NAME="dyndns"

# Step 1: Compile the application
echo "Compiling application..."
./gradlew clean build || { echo "Build failed! Exiting."; exit 1; }

# Step 2: Build the Docker image
echo "Building Docker image..."
docker build -t $IMAGE_NAME . || { echo "Docker build failed! Exiting."; exit 1; }

# Step 3: Start the database using Docker Compose
echo "Starting database container..."
docker-compose up -d || { echo "Failed to start database container! Exiting."; exit 1; }

# Step 4: Remove any existing application containers
if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
  echo "Removing existing application container..."
  docker rm -f $CONTAINER_NAME || { echo "Failed to remove existing container! Exiting."; exit 1; }
fi

# Step 5: Start the container and show only the program output
echo "Starting application container..."
docker run --rm -it \
  --name $CONTAINER_NAME \
  -v $(pwd)/config:/app/config \
  --network $(basename $(pwd))_default \
  $IMAGE_NAME
