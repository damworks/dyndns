#!/bin/bash

# Nome immagine e container
IMAGE_NAME="dyndns"
CONTAINER_NAME="dyndns"

# Step 1: Compila l'applicazione
echo "Compiling application..."
./gradlew clean build || { echo "Build failed! Exiting."; exit 1; }

# Step 2: Costruisci l'immagine Docker
echo "Building Docker image..."
docker build -t $IMAGE_NAME . || { echo "Docker build failed! Exiting."; exit 1; }

# Step 3: Rimuovi eventuali container esistenti
if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
  echo "Removing existing container..."
  docker rm -f $CONTAINER_NAME || { echo "Failed to remove existing container! Exiting."; exit 1; }
fi

# Step 4: Avvia il container mostrando solo l'output del programma
echo "Starting Docker container..."
docker run --rm -it \
  --name $CONTAINER_NAME \
  -v $(pwd)/config:/app/config \
  $IMAGE_NAME
