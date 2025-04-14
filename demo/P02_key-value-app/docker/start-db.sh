MONGODB_IMAGE="mongodb/mongodb-community-server"
MONGODB_TAG="7.0-ubuntu2204"
source .env.db

# Add root credentials to MongoDB container
ROOT_PASSWORD="rootuser"
ROOT_USER="rootpass"

# Key value DB credentials
KEY_VALUE_DB="key-value-db"
KEY_VALUE_USER="key-value-user"
KEY_VALUE_PASSWORD="key-value-password"

# Connection
HOST_PORT=27017
CONTAINER_PORT=27017
# have to create a network with: docker network create key-value-net
source .env.network

# Storage
source .env.volume

source setup.sh

if [ "$(docker ps -aq -f name=$DB_CONTAINER_NAME)" ]; then
    echo "Container $DB_CONTAINER_NAME already exists. Stopping and removing it."
    echo "Stop it with docker kill $DB_CONTAINER_NAME" # need to remove it if --rm is not used in run command
    # echo "Remove it with docker rm $DB_CONTAINER_NAME"
    exit 1
fi

docker run -d --rm --name $DB_CONTAINER_NAME \
    -e MONGO_INITDB_ROOT_USERNAME=$ROOT_USER \
    -e MONGO_INITDB_ROOT_PASSWORD=$ROOT_PASSWORD \
    -e KEY_VALUE_DB=$KEY_VALUE_DB \
    -e KEY_VALUE_USER=$KEY_VALUE_USER \
    -e KEY_VALUE_PASSWORD=$KEY_VALUE_PASSWORD \
    -v ./db-config/:/docker-entrypoint-initdb.d/ \
    -v $KEY_VALUE_VOLUME:/data/db \
    -p $HOST_PORT:$CONTAINER_PORT \
    --network $KEY_VALUE_NETWORK \
    $MONGODB_IMAGE:$MONGODB_TAG

# Connect to the MongoDB container using the following command:
# docker exec -it $DB_CONTAINER_NAME mongo --username $ROOT_USER --password $ROOT_PASSWORD --authenticationDatabase admin
# or
# docker run -it --rm --name debugsh --network key-value-net mongodb/mongodb-community-server:7.0-ubuntu2204 mongosh mongodb://key-value-user:key-value-password@mongodb/key-value-db
