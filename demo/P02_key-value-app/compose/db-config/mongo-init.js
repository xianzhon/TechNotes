const KeyValueDb = process.env.KEY_VALUE_DB;
const KeyValueUser = process.env.KEY_VALUE_USER;
const KeyValuePassword = process.env.KEY_VALUE_PASSWORD;

console.log('Initialize Key-Value DB');

db = db.getSiblingDB(KeyValueDb);

db.createUser({
  user: KeyValueUser,
  pwd: KeyValuePassword,
  roles: [
    { role: "readWrite", db: KeyValueDb }
  ]
});