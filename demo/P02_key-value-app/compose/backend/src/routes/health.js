
const express = require('express');

// define the health router
const healthRouter = express.Router();

// define the route
healthRouter.get('/', (req, res) => {
    res.send('up');
});

// export the router
module.exports = healthRouter;