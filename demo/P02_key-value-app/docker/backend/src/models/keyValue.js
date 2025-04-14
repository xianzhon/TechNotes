const mongoose = require('mongoose');

// We use mongoose.Schema to define the schema for the key-value pair. So that when define the model, we can use the schema to validate the data.
const keyValueSchema = new mongoose.Schema({
    key: { type: String, required: true, unique: true },
    value: { type: String, required: true }
});

// The mongoose model will provide a way to interact with the MongoDB database.
// It provides methods to create, read, update, and delete documents in the database.
// such as: KeyValue.create(), KeyValue.find(), KeyValue.findById(), KeyValue.update(), KeyValue.delete()
const KeyValue = mongoose.model('KeyValue', keyValueSchema);

// export the model so that it can be used in other files.
module.exports = KeyValue;