#!/bin/bash

# Prefix of container names
CONTAINER_PREFIX="dyndns"

# Find all running containers starting with the prefix
CONTAINERS=$(docker ps -q -f name="^${CONTAINER_PREFIX}")

if [ -n "$CONTAINERS" ]; then
  echo "Stopping containers with prefix '$CONTAINER_PREFIX'..."
  docker stop $CONTAINERS
else
  echo "No running containers found with prefix '$CONTAINER_PREFIX'."
fi