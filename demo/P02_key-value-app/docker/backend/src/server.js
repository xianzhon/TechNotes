const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');

// use the router from the store.js file. This is relative path.
const storeRouter = require('./routes/store');
const healthRouter = require('./routes/health');

const app = express();
app.use(bodyParser.json());

app.use('/store', storeRouter);
app.use('/health', healthRouter);

console.log('Connecting to MongoDB')
// If we use localhost, we can connect to the container because we are using the host network
// But if we use the container name: mongodb, we need to make the backend run in the container which has same network as the MongoDB container
mongoose.connect(`mongodb://${process.env.MONGODB_HOST}:27017/${process.env.KEY_VALUE_DB}`, {
    auth: {
        username: `${process.env.KEY_VALUE_USER}`,
        password: `${process.env.KEY_VALUE_PASSWORD}`
    },
    connectTimeoutMS: 500
}).then(() => {
    console.log('Connected to MongoDB');
    app.listen(3000, () => {
        console.log('Server is running on port 3000');
    });
}).catch(err => {
    console.error('Error connecting to MongoDB:', err);
});