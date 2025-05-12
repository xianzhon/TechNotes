// example from: https://graphql.org/graphql-js/
// This code is used to run a GraphQL query and print out the response.

var { graphql, buildSchema } = require("graphql")

// Construct a schema, using GraphQL schema language
var schema = buildSchema(`
  type Query {
    hello: String
  }
`)

// The rootValue provides a resolver function for each API endpoint
var rootValue = {
  hello() {
    return `Hello world!`
  }
}

// Run the GraphQL query '{ hello }' and print out the response
graphql({
  schema,
  source: "{ hello }",
  rootValue
}).then(response => {
  console.log(response)
})
