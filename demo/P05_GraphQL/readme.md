# Study GraphQL

## GraphQL vs REST

GraphQL is a query language for APIs and a runtime for fulfilling those queries with existing data. It provides a better way to fetch data from a server than RESTful APIs because it allows clients to specify exactly what data they need, and it returns only the data they need, reducing the amount of data transferred over the network.

RESTful APIs, on the other hand, are designed around resources and HTTP verbs, and they return all the data for a resource whenever a client requests it. This can be inefficient for large datasets, as clients may only need a small portion of the data.

In summary, GraphQL is a better fit for fetching data from a server than RESTful APIs for the following reasons:

- It allows clients to specify exactly what data they need, reducing the amount of data transferred over the network.
- It returns only the data they need, reducing the amount of data returned to the client.
- It is easier to use than RESTful APIs, as clients can use a familiar syntax to specify the data they need.
- It is more flexible than RESTful APIs, as it allows clients to fetch data from multiple resources in a single request.


## GraphQL in details

### Basic concepts

- **Schema**: A GraphQL schema defines the structure of the data available on the server. It defines the available types and fields on the server, as well as the relationships between them.
- **Type**: A type is an object that defines a set of fields. Each field is a function that returns a value for that field.
- **Field**: A field is a function that returns a value for a type. It takes arguments (if any) and returns a value of the type defined by the field.
- **Resolver**: A resolver is a function that takes a field's arguments and returns the value for that field. It is responsible for fetching the data for the field from the underlying data source.
- **Query**: A query is a request to the server for data. It specifies the fields to be returned and any arguments to be passed to the resolvers.
- **Mutation**: A mutation is a request to the server to modify data. It specifies the fields to be modified and any arguments to be passed to the resolvers.
- **Subscription**: A subscription is a request to the server to receive real-time updates for a set of fields. It specifies the fields to be updated and any arguments to be passed to the resolvers.

### GraphQL server

A GraphQL server is an HTTP server that exposes a GraphQL API. It can be implemented using various server-side frameworks, such as Express, Hapi, or Koa.

The GraphQL server can be configured to use various data sources, such as databases, file systems, or other APIs. It can also be configured to use authentication and authorization mechanisms, such as JSON Web Tokens (JWT) or OAuth 2.0.

The GraphQL server can be used to serve both GraphQL queries and mutations, as well as subscriptions. It can also be used to serve static files, such as HTML, CSS, and JavaScript, to the client.

### GraphQL client

A GraphQL client is a tool that can be used to send queries and mutations to a GraphQL server. It can be implemented using various client-side frameworks, such as Apollo Client, Relay, or urql.

The GraphQL client can be configured to use various transport protocols, such as HTTP, WebSockets, or GraphQL Subscriptions. It can also be configured to use authentication and authorization mechanisms, such as JWT or OAuth 2.0.

The GraphQL client can be used to send queries and mutations to the GraphQL server, as well as subscribe to real-time updates using subscriptions. It can also be used to fetch and cache data from the server, reducing the amount of data transferred over the network.

### GraphQL ecosystem

GraphQL has a large and active ecosystem, with many tools, libraries, and services available. Here are some of the most popular ones:

- **GraphQL Tools**: A set of tools for building and maintaining GraphQL servers and clients.
- **GraphQL Playground**: A web-based IDE for exploring and testing GraphQL APIs.
- **GraphQL Code Generator**: A tool for generating code from a GraphQL schema.
- **GraphQL Mesh**: A tool for building GraphQL gateways that connect multiple GraphQL APIs together.
- **GraphQL Faker**: A tool for generating fake data for testing and development.
- **GraphQL Inspector**: A tool for analyzing GraphQL APIs and detecting performance issues.
- **GraphQL Voyager**: A tool for visualizing GraphQL schemas and exploring the data.
- **GraphQL Lint**: A tool for linting GraphQL queries and mutations.


### GraphQL Basic Types

The GraphQL schema language supports the scalar types of `String, Int, Float, Boolean, and ID`, so you can use these directly in the schema you pass to buildSchema.

By default, every type is nullable - it’s legitimate to return null as any of the scalar types. Use an exclamation point to indicate a type cannot be nullable, so String! is a non-nullable string.

To use a list type, surround the type in square brackets, so `[Int]` is a list of integers.

Here are some examples of using basic types in the schema:
```
type Query {
  hello: String!
  greeting(name: String!): String!
  age: Int!
  pi: Float!
}
```

In this example, the Query type has three fields: `hello`, `greeting`, and `age`. The `hello` field returns a non-nullable string, while the `greeting` field takes a non-nullable string argument and returns a non-nullable string. The `age` field returns a non-nullable integer, and the `pi` field returns a non-nullable float.


## 实操

### 准备开发环境

Create a new directory for the project:
```
mkdir HelloGraphQL
cd HelloGraphQL

npm init -y
npm install express graphql graphql-http ruru
```

Hot reload:
```
npm install nodemon --save-dev
# 这种安装方式，无法在命令行直接使用nodemon，需要依赖package.json的scripts字段配置，例如:
   "scripts": {
    "dev": "nodemon server1.js",
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  Then run in terminal: npm run dev

Or we can install globally: npm install -g nodemon
```

### Practice GraphQL with Express

- example: server1.js
// example from: https://graphql.org/graphql-js/

- example: server_types.js
// An example to demostrate the usage of basic data types
// from: https://graphql.org/graphql-js/basic-types/

- example: server.js
// example from: https://graphql.org/graphql-js/running-an-express-graphql-server/
// This code is used to create an Express server that runs a GraphQL API.