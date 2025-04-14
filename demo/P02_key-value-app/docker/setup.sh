source .env.network
source .env.volume

if [ "$(docker network ls -q -f name=$KEY_VALUE_NETWORK)" ]; then
    echo "Network $KEY_VALUE_NETWORK already exists. Skipping creation."
else
    docker network create $KEY_VALUE_NETWORK
    echo "Network $KEY_VALUE_NETWORK created."
fi

if [ "$(docker volume ls -q -f name=$KEY_VALUE_VOLUME)" ]; then 
    echo "Volume $KEY_VALUE_VOLUME already exists. Skipping creation."
else
    docker volume create $KEY_VALUE_VOLUME
    echo "Volume $KEY_VALUE_VOLUME created."
fi