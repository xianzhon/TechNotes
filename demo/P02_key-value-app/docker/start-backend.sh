BACKEND_IMAGE="key-value-backend"
# source .env.backend
MONGODB_HOST="mongodb"
BACKEND_CONTAINER_NAME="backend"

# Key value DB credentials
KEY_VALUE_DB="key-value-db"
KEY_VALUE_USER="key-value-user"
KEY_VALUE_PASSWORD="key-value-password"

# Connection
HOST_PORT=3000
CONTAINER_PORT=3000
# have to create a network with: docker network create key-value-net
source .env.network

# Storage

if [ "$(docker ps -aq -f name=$BACKEND_CONTAINER_NAME)" ]; then
    echo "Container $BACKEND_CONTAINER_NAME already exists. Stopping and removing it."
    echo "Stop it with docker kill $BACKEND_CONTAINER_NAME" # need to remove it if --rm is not used in run command
    # echo "Remove it with docker rm $BACKEND_CONTAINER_NAME"
    exit 1
fi

docker build -t $BACKEND_IMAGE \
  -f backend/Dockerfile.dev \
  backend

docker run --rm -d --name $BACKEND_CONTAINER_NAME \
    -e MONGODB_HOST=$MONGODB_HOST \
    -e KEY_VALUE_DB=$KEY_VALUE_DB \
    -e KEY_VALUE_USER=$KEY_VALUE_USER \
    -e KEY_VALUE_PASSWORD=$KEY_VALUE_PASSWORD \
    -v ./backend/src:/app/src \
    -p $HOST_PORT:$CONTAINER_PORT \
    --network $KEY_VALUE_NETWORK \
    $BACKEND_IMAGE
