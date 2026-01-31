#!/bin/bash
set -e

CONTAINER_NAME="pokedex-fun-apis"
IMAGE_NAME="pokedex-fun-apis"
PORT=8080

echo "Cleaning up existing container..."
docker rm -f $CONTAINER_NAME 2>/dev/null || true

echo "Building $IMAGE_NAME..."
docker build -t $IMAGE_NAME .

echo "Starting $CONTAINER_NAME..."
docker run -d -p $PORT:$PORT --name $CONTAINER_NAME $IMAGE_NAME

echo "Waiting for server to start..."
sleep 10

echo "Testing health endpoint..."
HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:$PORT/health)

if [ "$HTTP_STATUS" -eq 200 ]; then
    echo -e "Health check passed (HTTP $HTTP_STATUS)"
else
    echo -e "Health check failed (HTTP $HTTP_STATUS)"
fi

echo "Stopping $CONTAINER_NAME..."
docker stop $CONTAINER_NAME
echo "Removing $CONTAINER_NAME..."
docker rm $CONTAINER_NAME
echo "Removing image $IMAGE_NAME..."
docker rmi $IMAGE_NAME
echo "Done."