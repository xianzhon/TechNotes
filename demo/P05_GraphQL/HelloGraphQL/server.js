// example from: https://graphql.org/graphql-js/running-an-express-graphql-server/
// This code is used to create an Express server that runs a GraphQL API.

var express = require("express")
var { createHandler } = require("graphql-http/lib/use/express")
var { buildSchema } = require("graphql")
var { ruruHTML } = require("ruru/server")

// Construct a schema, using GraphQL schema language
var schema = buildSchema(`
  type Query {
    hello: String
  }
`)

// The root provides a resolver function for each API endpoint
var root = {
  hello() {
    return "Hello world!"
  },
}

var app = express()

// Create and use the GraphQL handler.
app.all(
  "/graphql",
  createHandler({
    schema: schema,
    rootValue: root,
  })
)

// Serve the GraphiQL IDE.
app.get("/", (_req, res) => {
  res.type("html")
  res.end(ruruHTML({ endpoint: "/graphql" }))
})

// Start the server at port
app.listen(4000)
console.log("Running a GraphQL API server at http://localhost:4000/graphql")

/*
Run:
node server1.js
nodemon server1.js

Test:
1. query with curl:
request:
  curl -X POST \
    -H "Content-Type: application/json" \
    -d '{"query": "{ hello }"}' \
    http://localhost:4000/graphql

response:
  {"data":{"hello":"Hello world!"}}

2. query in browser with GraphiQL:
  http://localhost:4000/

3. query with Postman:

query body:
{
    "query": "{hello}"
}
*/