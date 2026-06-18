#!/bin/bash
docker compose -f ./docker/docker-compose.yaml down
docker rmi vtaxer-core

# Build the Docker image
docker build -t vtaxer-core .

# Run the container
# docker run -p 8080:8080 vtaxer

docker compose -f ./docker/docker-compose.yaml up -d
