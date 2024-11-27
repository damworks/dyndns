#!/bin/bash

# Container name
CONTAINER_NAME="dyndns-updater"

# Check if the container is running
if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
  echo "Stopping container $CONTAINER_NAME..."
  docker stop $CONTAINER_NAME
else
  echo "No running container found with name $CONTAINER_NAME."
fi
