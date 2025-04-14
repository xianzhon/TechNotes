# Key-value-app

Develop a key-value-app using NodeJs + MongoDB + docker/docker-compose.

1. get to know docker + RestfulAPI

2. be familar with MongoDB

3. be familiar with Restful API practice

4. study docker-compose

	

# V1. [docker] Project Setup and Deploy App with Data Persistence

### Start MongoDB container

phase 1: 在命令行中使用 docker 命令

```bash
docker pull mongodb/mongodb-community-server:7.0-ubuntu2204
docker run -d --name mongodb mongodb/mongodb-community-server:7.0-ubuntu2204
docker exec -it mongodb mongosh
    show dbs;
    use admin;
    show collections;
```

phase 2:  使用 shell 脚本管理 docker 命令

```bash
start-db.sh
cleanup-db.sh
```

### Add root authentification for mongodb docker

Very easy, just pass the environment variable when start the container.

```
    -e MONGO_INITDB_ROOT_USERNAME=$ROOT_USER \
    -e MONGO_INITDB_ROOT_PASSWORD=$ROOT_PASSWORD \
```

### When work without docker-compose

We need to create network, volume manually.

```bash
docker volume create key-value-volume
docker network create key-value-net
```

### Setup express API service

```bash
mkdir backend
cd backend
npm init -y
npm i express@4.19.2 mongoose@8.5.1 body-parser@1.20.2 --save-exact
echo "node_modules" > .dockerignore
```

#### Dockerizing the Express App

```bash
docker build -t key-value-backend -f Dockerfile.dev .
# -f used to specify which Dockerfile to use.
# -t the target image name
# last .: the whole directory


docker run -d --name backend --network key-value-net -p 3000:3000 key-value-backend
# To check whether the backend connected to the database
docker logs backend

# check the backend is up
curl http://localhost:3000/health
```

#### Add Hot reloading with Nodemon

1. install the `nodemon` package

```bash
# what does save-exact do?
# save-exact: save the exact version of the package in package.json, instead of using the latest version.
# if not given, npm will install the latest version of the package, which may cause incompatibility.
npm i nodemon@3.1.4 --save-dev --save-exact
```

2. add the following script to package.json

```json
"scripts": {
    "dev": "nodemon src/server.js"
}
```

3. change the Dockerfile.dev to use the dev command

```bash
# Hot reloading for development
CMD ["npm", "run", "dev"]
```

Previously we use `npm start`, it is just a shortcut for `npm run start`.

4. Map the source code to the container, so that the changes in the source code will trigger the nodemon in docker container to restart the server

```bash
docker run --rm -d --name $BACKEND_CONTAINER_NAME \
    -e MONGODB_HOST=$MONGODB_HOST \
    -e KEY_VALUE_DB=$KEY_VALUE_DB \
    -e KEY_VALUE_USER=$KEY_VALUE_USER \
    -e KEY_VALUE_PASSWORD=$KEY_VALUE_PASSWORD \
    -v ./backend/src:/app/src \                    # map the source code to the container
    -p $HOST_PORT:$CONTAINER_PORT \
    --network $KEY_VALUE_NETWORK \
    $BACKEND_IMAGE
```

5. verify the hot reloading by changing the code and save the file. The server should restart automatically.

```bash
# Use -f will like `tail -f`, it will follow the log output.
$ docker logs -f backend

> backend@1.0.0 dev
> nodemon src/server.js

[nodemon] 3.1.4
[nodemon] to restart at any time, enter `rs`
[nodemon] watching path(s): *.*
[nodemon] watching extensions: js,mjs,cjs,json
[nodemon] starting `node src/server.js`
Connecting to MongoDB
Connected to MongoDB
Server is running on port 3000
[nodemon] restarting due to changes...
[nodemon] starting `node src/server.js`
Connecting to MongoDB
Connected to MongoDB
Server is running on port 3000
```

#### Defining the API Routes

1. first add some sample routes to the server.js

```javascript
app.get('/store/:key', (req, res) => {
    return res.send('get value for key:' + req.params.key);
})
app.post('/store/:key', (req, res) => {
    return res.send('set value for key:' + req.params.key + 'to value:' + req.body.value);
})
app.put('/store/:key', (req, res) => {
    return res.send('update value for key:' + req.params.key + 'to value:' + req.body.value);
})
app.delete('/store/:key', (req, res) => {
    return res.send('delete value for key:' + req.params.key);
})
```

2. Test the API routes with postman or curl

```bash
curl -X POST http://localhost:3000/store/key1 -d '{"value": "value1"}'
curl -X GET http://localhost:3000/store/key1
curl -X PUT http://localhost:3000/store/key1 -d '{"value": "value2"}'
curl -X DELETE http://localhost:3000/store/key1
```

3. then we realize that the routes are all in server.js, which is not a good practice. We should separate the routes into a separate file.

- create sub-folder src/routes
- create two files: store.js and health.js which contains the routes for two groups of APIs.
- Implement routes in each group with express.Router(), and export the router.
- In server.js, import the router and use it to define the routes (it can define the prefix for each group of APIs, so in each router file, no need to add the prefix).

#### Creating and Retrieving Key-value Pairs

We could use the mongoose package to interact with MongoDB to replace the mock implementation of key-value API.

1. First needs to define the schema for the key-value pair.
   We put the schema and model under src/models/keyValue.js.

```javascript
const mongoose = require('mongoose');

const keyValueSchema = new mongoose.Schema({
    key: { type: String, required: true, unique: true },
    value: { type: String, required: true }
});

const KeyValue = mongoose.model('KeyValue', keyValueSchema);

module.exports = KeyValue;
```

2. Implement the routes for creating, retrieving, updating, and deleting key-value pairs. Just take care of the usage of mongoose API
   and error handling. (file: store.js)

```javascript
// Note1: get params from the request object
    // 1. extract param from request path param: key = req.params.key
    // 2. extract param from request body: {key, value} = req.body
    // 3. how to extract param from request query string: ?key=value.  answer is: key = req.query.key

// Note 2: use async/await to handle async operations
    // 1. mongoose API returns a promise, so we need to use async/await to handle it.
    // 2. use try/catch to handle errors.
    // 3. Query API: await model.findOne({key}); find, findById
    // 4. Create API: await model.save()
    // 5. Update API: await model.findOneAndUpdate({key}, {value}, {new: true}); (1st param: query, 2nd param: update, 3rd param: options, return the updated document)
    // 6. Delete API: await model.findOneAndDelete({key}); (return the deleted document)

// Note 3: use res.status() to set the response status code. the .json() method to send the response body. Add a return statement to prevent any further code execution.
    // 1. res.status(200).json({key, value});
    // 2. res.status(400).json({error: 'Invalid request.'});
    // 3. res.status(404).json({error: 'Key not found.'});
    // 4. res.status(500).json({error: 'Internal server error.'});
```

#### Updating and Deleting Key-value Pairs

Same as above section.

# V2. Refactor using docker compose

### compose command

Basically the docker-compose tool and docker compose plugin have same functionality. Use either one is fine.

```bash
$ docker compose version   # this is the plugin installed with docker desktop
Docker Compose version v2.32.4

$docker-compose  --version  # this is the separate compose tool
Docker Compose version v2.32.4
```

### Run MongoDB with Docker Compose

1. prepare a compose.yaml file. The old name docker-compose.yaml is deprecated.

```bash
services:
  db:
    image: mongodb/mongodb-community-server:7.0-ubuntu2204
    ports:
      - 27017:27017
```

2. then run the compose up command to start the MongoDB container.

```bash
$ dc up
[+] Running 2/2
 ✔ Network compose_default  Created
 ✔ Container compose-db-1   Created
```

By default, the container name & network name uses the folder name as prefix.

3. then check the container status (logs & network)
   `docker network ls`
4. connect to the MongoDB container using the mongo shell from the same network

```bash
# checkt the network name and container name from step 3.
$ docker run -it --rm --name debugsh --network compose_default mongodb/mongodb-community-server:7.0-ubuntu2204 mongosh mongodb://compose-db-1/key-value-db
    show databases;
    use admin;
    show collections;
```

### Use Environment Variables in Compose File

We can use environment variables in the compose file to make the configuration more flexible.

```bash
services:
  db:
    image: ${MONGODB_IMAGE}:${MONGODB_TAG}
    ports:
      - ${HOST_PORT}:${CONTAINER_PORT}
    environment:
      - MONGO_INITDB_ROOT_USERNAME=root
      - MONGO_INITDB_ROOT_PASSWORD=root_password
```

但是直接把敏感信息放到compose文件中，不太安全。所以，最好把敏感信息放到.env文件中，然后通过.env文件加载到compose文件中。而这个.env文件一般是不会被提交到 git 的。

正确的做法是，创建.env.db 文件，然后把yaml文件中敏感信息都放到.env.db文件中。然后在compose文件中通过env_file加载.env.db文件。这样，敏感信息就不会暴露在compose文件中。

```bash
services:
  db:
    image: ${MONGODB_IMAGE}:${MONGODB_TAG}
    ports:
      - ${HOST_PORT}:${CONTAINER_PORT}
    env_file:
      - .env.db
```

另外环境变量文件可以加载多个。

### Work with bind mounts in docker-compose

1. we need to map the init script to the db container, in compose, we have two ways to do this:
   - use the bind keyword in the volumes section
   - use similar syntax as docker run command

```
...
    volumes:
    # method 1: prefer this one, as it is more explicit
      - type: bind
        source: ./db-config/mongo-init.js
        target: /docker-entrypoint-initdb.d/mongo-init.js
        readonly: true
    # method 2:
      - "./db-config/mongo-init.js:/docker-entrypoint-initdb.d/mongo-init.js:ro"
```

2. start the compose with: `dc up`
3. check the logs to see if the init script is executed. `dc logs | grep ross`
4. check db connection to the db container with the credentials from the init script.

```bash
docker run -it --rm --name debugsh --network compose_default mongodb/mongodb-community-server:7.0-ubuntu2204 mongosh mongodb://key-value-user:key-value-password@compose-db-1/key-value-db
# note, if we have a typo and the credential, then the connection will fail. which means our init script is working.
```

### Manage volumes and networks in docker-compose

1. From above, we can see composer creates a default network with the folder name as prefix. We can also specify the network in the compose file.
2. check the compose.yaml file for the grammar

```yaml
webservers:
    ...
    volumes:
      - type: bind   # this way is more explicit
        source: ./db-config/mongo-init.js
        target: /docker-entrypoint-initdb.d/mongo-init.js
      - type: volume
        source: mongodb-data
        target: /data/db
    networks:
      - key-value-net
# specify network
networks:
  key-value-net:

# specify volumes
volumes:
  mongodb-data:
```

3. we can specify the prefix for the resources in the compose file by specifying the project name.
   Note: to destroy all resources before any changes on the yaml file, use `dc down -v` to remove the volumes.
4. start the compose with: `dc up -d`

```bash
$ dc up -d
 ✔ Network key-value-store_key-value-net  Created                                                             0.1s
 ✔ Volume "key-value-store_mongodb-data"  Created                                                             0.0s
 ✔ Container key-value-store-db-1         Started                                                             0.3s

$ dc down -v
 ✔ Container key-value-store-db-1         Removed                                                            10.3s
 ✔ Volume key-value-store_mongodb-data    Removed                                                             0.0s
 ✔ Network key-value-store_key-value-net  Removed                                                             0.1s
```

要点 1：可以看到 compose 把容器和网络、存储之间的依赖关系管理得非常棒。不需要操心容器的启动顺序，compose 能自动处理。也不需要关心容器销毁时的清理顺序，compose 也能自动处理。
要点 2：如果在 yaml 文件中定义了volume , 但是在web service 容器中并没有引用的话，compose 不会自动创建这个volume

### Add a backend service to the docker-compose

1. add the backend service to the compose file, it includes the step to build the image from the backend folder
2. run the compose up command with build option: `dc up --build`
3. check the logs and RestfulAPI from postman

### Hot reloading and file watching with Nodemon

1. we can use the bind option. But according to the video author, this might not work well with nodemon.
2. so he suggests to use the watch feature provided by compose.

```yaml
    develop:
      watch:
        - action: sync
          path: ./backend
          target: /app
          ignore:
            - node_modules
            - .git
```

Or just use the following which is more specific to the src folder:

```yaml
    develop:
      watch:
        - action: sync
          path: ./backend/src
          target: /app/src
```

3. then run the compose up with --watch option

```bash
dc up --build --watch
```

4. check the logs to see if the nodemon is watching the file changes.
   we can add one route to the / to say some welcome message.

```javascript
app.get('/', (req, res) => {
    res.send('Welcome to the key-value store API');
});
```

5. after test, clean up the container and network, and the volumes with `dc down -v`

```bash
$ dc down -v
[+] Running 4/4
 ✔ Container key-value-store-backend-1    Removed                                                             0.4s
 ✔ Container key-value-store-db-1         Removed                                                            10.3s
 ✔ Volume key-value-store_mongodb-data    Removed                                                             0.0s
 ✔ Network key-value-store_key-value-net  Removed                                                             0.1s
```

### Use docker compose CLI

```bash
# First of all, know how to get the help
dc --help
dc down --help   (check -v option, check --remove-orphans option which is useful when we rename the service in the compose file and caused orphan container)

# list all services managed by the compose file (compared the result with docker ps)
# it will also list the service column which shows the name of the service defined in the compose file.
$ dc ps
NAME                        IMAGE                                             COMMAND                  SERVICE   CREATED         STATUS        PORTS
key-value-store-backend-1   key-value-store-backend                           "docker-entrypoint.s…"   backend   2 seconds ago   Up 1 second   0.0.0.0:3000->3000/tcp, :::3000->3000/tcp
key-value-store-db-1        mongodb/mongodb-community-server:7.0-ubuntu2204   "python3 /usr/local/…"   db        2 seconds ago   Up 1 second   0.0.0.0:27017->27017/tcp, :::27017->27017/tcp

# start a service
dc up -d    # start all services in detached mode
dc start db
dc start backend  # although we start only backend, it will start all the dependencies, including db. That's really cool!!

# stop a service
dc stop  # this will stop all the services
dc stop db
dc stop backend

dc down # stop and remove containers, networks (but not volumes)
dc down -v  # stop and remove containers, networks, and volumes

$ dc logs backend  # check log only for the backend service
```
