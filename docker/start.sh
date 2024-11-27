#!/bin/bash

# Image and container names
IMAGE_NAME="dyndns:latest"
CONTAINER_NAME="dyndns-updater"

# Build the Docker image
echo "Building Docker image..."
docker build -t $IMAGE_NAME ./docker

# Remove the existing container if it exists
if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
  echo "Removing existing container..."
  docker rm -f $CONTAINER_NAME
fi

# Start a new container
echo "Starting Docker container..."
docker run --rm -d \
  --name $CONTAINER_NAME \
  -v $(pwd)/config:/app/config \
  $IMAGE_NAME
