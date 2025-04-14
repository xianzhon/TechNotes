const express = require('express');
const KeyValue = require('../models/keyValue');

// define the store router
const storeRouter = express.Router();

// define the routes
storeRouter.post('/', async (req, res) => {
    // Extract the key and value from the request body
    const {key, value} = req.body;
    // Add validation for the key and value here
    if (!key || !value) {
        return res.status(400).send({message: 'Key and value are required.'});
    }

    try {
        // Check if the key already exists in the database
        const existingKeyValue = await KeyValue.findOne({key});
        if (existingKeyValue) {
            // If the key already exists, send a 400 response. 400 means Bad Request.
            return res.status(400).send({message: 'Key already exists.'});
        }

        // Create a new key-value pair
        const keyValue = new KeyValue({key, value});
        // Save the key-value pair to the database
        await keyValue.save();
        // Send a 201 response with the key-value pair, 201 means resource Created.
        // Do we need a return here?
        return res.status(201).json({message: 'Key-value pair stored successfully.'});
    } catch (err) {
        // Do we need a return here? why?
        // Including a return statement is beneficial for clarity and to prevent any further code execution:
        // This way, once an error response is sent, the function will terminate, and any code after this line will not be executed.
        return res.status(500).send({error: 'Internal server error.'});
    }
});

storeRouter.get('/:key', async (req, res) => {
    const key = req.params.key;
    if (!key) {
        return res.status(400).send({error: 'Key is required.'});
    }
    try {
        // Find the key-value pair with the given key
        const keyValue = await KeyValue.findOne({key});
        if (!keyValue) {
            return res.status(404).send({error: 'Key not found.'});
        }
        // why not use keyValue directly? because it is an object from DB, it contains the id and other metadata
        return res.status(200).json({key, value: keyValue.value});
    } catch (err) {
        // Including a return statement is beneficial for clarity and to prevent any further code execution:
        return res.status(500).send({error: 'Internal server error.'});
    }
});

storeRouter.put('/:key', async (req, res) => {
    // Extract the key and value from the request body
    const {key} = req.params;
    const {value} = req.body;
    if (!value) {
        return res.status(400).send({error: 'The value is required.'});
    }
    try {
        // Find the key-value pair with the given key
        const keyValue = await KeyValue.findOneAndUpdate({key}, {value}, {new: true});
        if (!keyValue) {
            // If the key does not exist, send a 404 response
            return res.status(404).json({error: 'Key not found.'});
        }

        // Send a 200 response with the updated key-value pair
        return res.status(200).json({message: 'Key-value pair updated successfully.', key: keyValue.key, value: keyValue.value});
    } catch (err) {
        return res.status(500).send({error: 'Internal server error.'});
    }
});

storeRouter.delete('/:key', async (req, res) => {
    const key = req.params.key;
    if (!key) {
        return res.status(400).send({error: 'Key is required.'});
    }
    try {
        // Find the key-value pair with the given key
        const keyValue = await KeyValue.findOneAndDelete({key});
        if (!keyValue) {
            // If the key does not exist, send a 404 response
            return res.status(404).json({error: 'Key not found.'});
        }

        // Send a 200 response with the deleted key-value pair
        return res.status(200).json({message: 'Key-value pair deleted successfully.'});
    } catch (err) {
        return res.status(500).send({error: 'Internal server error.'});
    }
});

// export the router
module.exports = storeRouter;