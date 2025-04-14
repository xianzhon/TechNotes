const KeyValueDb = process.env.KEY_VALUE_DB || 'key-value-app';
const KeyValueUser = process.env.KEY_VALUE_USER;
const KeyValuePassword = process.env.KEY_VALUE_PASSWORD;

db = db.getSiblingDB(KeyValueDb);

db.createUser({
  user: KeyValueUser,
  pwd: KeyValuePassword,
  roles: [
    { role: "readWrite", db: KeyValueDb }
  ]
});