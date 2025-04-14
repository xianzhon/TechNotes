source .env.network
source .env.volume
source .env.db

# Stop and remove the container if it exists
if [ "$(docker ps -q -f name=$DB_CONTAINER_NAME)" ]; then
    echo "Stopping and removing container $DB_CONTAINER_NAME..."
    docker kill $DB_CONTAINER_NAME
    # docker rm $DB_CONTAINER_NAME # this is not needed if we use the --rm option in the docker run command
else
    echo "Container $DB_CONTAINER_NAME does not exist. Skipping delete."
fi

# Remove the volume if it exists
if [ "$(docker volume ls -q -f name=$KEY_VALUE_VOLUME)" ]; then
    echo "Removing volume $KEY_VALUE_VOLUME..."
    docker volume rm $KEY_VALUE_VOLUME
else
    echo "Volume $KEY_VALUE_VOLUME does not exist. Skipping remove."
fi

# Remove the network if it exists
if [ "$(docker network ls -q -f name=$KEY_VALUE_NETWORK)" ]; then
    echo "Removing network $KEY_VALUE_NETWORK..."
    docker network rm $KEY_VALUE_NETWORK
else
    echo "Network $KEY_VALUE_NETWORK does not exist. Skipping remove."
fi