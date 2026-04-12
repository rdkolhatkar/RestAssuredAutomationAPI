# 📘 GraphQL — Complete Guide for QA Testers & Developers

> **Difficulty Level:** Beginner to Intermediate  
> **Audience:** QA Testers, Frontend & Backend Developers  
> **Last Updated:** April 2026

---

## 📚 Table of Contents

1. [What is GraphQL?](#1-what-is-graphql)
2. [GraphQL vs REST APIs](#2-graphql-vs-rest-apis)
3. [How GraphQL Works — The Big Picture](#3-how-graphql-works--the-big-picture)
4. [Core GraphQL Terminologies](#4-core-graphql-terminologies)
5. [GraphQL Schema — The Blueprint](#5-graphql-schema--the-blueprint)
6. [GraphQL Queries — Fetching Data](#6-graphql-queries--fetching-data)
7. [GraphQL Mutations — Modifying Data](#7-graphql-mutations--modifying-data)
8. [GraphQL Subscriptions — Real-time Data](#8-graphql-subscriptions--real-time-data)
9. [Testing GraphQL — Step-by-Step Guide](#9-testing-graphql--step-by-step-guide)
10. [Testing Mutations — Step-by-Step Guide](#10-testing-mutations--step-by-step-guide)
11. [Common Errors & How to Debug](#11-common-errors--how-to-debug)
12. [Quick Reference Cheat Sheet](#12-quick-reference-cheat-sheet)

---

## 1. What is GraphQL?

### 🔍 Simple Definition

**GraphQL** is a **query language for APIs** and a **runtime for executing those queries**.  
It was created by **Facebook (Meta) in 2012** and open-sourced in **2015**.

Think of GraphQL like ordering food at a restaurant:

> 🍔 **REST API** = You get a fixed combo meal (burger + fries + drink), even if you only wanted the burger.  
> 🍔 **GraphQL** = You customize your order — you ask for exactly what you want and nothing more.

### 💡 Why was GraphQL created?

Facebook needed a way to load data efficiently for their mobile app.  
REST APIs were returning **too much data** (over-fetching) or **not enough data** (under-fetching), requiring multiple round trips to different endpoints.

GraphQL solved this by letting the **client decide exactly what data it needs** in a **single request**.

---

## 2. GraphQL vs REST APIs

### 🆚 Side-by-Side Comparison

| Feature | REST API | GraphQL |
|---|---|---|
| **Endpoints** | Multiple endpoints (`/users`, `/posts`, `/comments`) | Single endpoint (`/graphql`) |
| **Data Fetching** | Fixed response structure | Client requests exactly what it needs |
| **Over-fetching** | ✅ Common problem | ❌ Never — you get only what you ask |
| **Under-fetching** | ✅ Common (multiple requests needed) | ❌ Never — one request can get all related data |
| **Versioning** | Needs `/v1/`, `/v2/` endpoints | No versioning needed — schema evolves |
| **Request Type** | GET, POST, PUT, DELETE, PATCH | Mostly POST (with Query/Mutation in body) |
| **Documentation** | External (Swagger/OpenAPI) | Built-in via Introspection & Schema |
| **Error Handling** | HTTP Status Codes (200, 404, 500) | Always 200 OK; errors inside response body |
| **Real-time** | Needs WebSockets/SSE setup | Built-in Subscriptions |
| **Learning Curve** | Lower | Slightly higher (but worth it) |

---

### 📊 Visual Diagram: REST vs GraphQL Data Fetching

```
════════════════════════════════════════════════════════════════════════
                      REST API APPROACH
════════════════════════════════════════════════════════════════════════

  Client                          Server
    │                               │
    │── GET /users/1 ──────────────>│  Request 1: Get User
    │<─ { id, name, email,          │
    │     address, phone,           │  ⚠️  Over-fetching:
    │     createdAt, updatedAt }   │  Client only wanted name & email
    │                               │  but got everything!
    │── GET /users/1/posts ────────>│  Request 2: Get Posts
    │<─ { posts: [...] } ──────────│
    │                               │
    │── GET /users/1/followers ───>│  Request 3: Get Followers
    │<─ { followers: [...] } ──────│
    │                               │
    │   3 separate API calls needed │
    │   for one screen! ❌          │
    │                               │

════════════════════════════════════════════════════════════════════════
                    GRAPHQL APPROACH
════════════════════════════════════════════════════════════════════════

  Client                          Server
    │                               │
    │── POST /graphql ─────────────>│
    │   Body: {                     │  Single Request ✅
    │     query {                   │
    │       user(id: "1") {         │  Client asks for EXACTLY:
    │         name                  │  - name
    │         email                 │  - email
    │         posts { title }       │  - post titles
    │         followers { name }    │  - follower names
    │       }                       │
    │     }                         │
    │   }                           │
    │                               │
    │<─ { "data": {                 │
    │       "user": {               │  Gets exactly what was asked ✅
    │         "name": "Alice",      │  Nothing more, nothing less ✅
    │         "email": "a@b.com",   │
    │         "posts": [...],       │
    │         "followers": [...]    │
    │       }                       │
    │     }                         │
    │   }                           │
════════════════════════════════════════════════════════════════════════
```

---

### 📝 Real-World Example: Loading a User Profile Page

**Scenario:** You are building a user profile page that shows:
- User's name and email
- Their last 3 blog posts (just the titles)
- Number of followers

**With REST API:**
```
Call 1: GET /api/users/42
Call 2: GET /api/users/42/posts?limit=3
Call 3: GET /api/users/42/followers/count
```
→ 3 network calls, possible over-fetching of user data

**With GraphQL:**
```graphql
# Single call to POST /graphql
query {
  user(id: "42") {
    name
    email
    posts(limit: 3) {
      title
    }
    followerCount
  }
}
```
→ 1 network call, get exactly what you need ✅

---

## 3. How GraphQL Works — The Big Picture

```
════════════════════════════════════════════════════════════════════════
                    GraphQL ARCHITECTURE
════════════════════════════════════════════════════════════════════════

  ┌─────────────┐        ┌──────────────────────────────────────────┐
  │             │        │           GraphQL SERVER                 │
  │   CLIENT    │        │                                          │
  │ (Browser /  │        │  ┌─────────────┐   ┌─────────────────┐  │
  │  Mobile App)│        │  │   SCHEMA    │   │   RESOLVERS     │  │
  │             │        │  │             │   │                 │  │
  │  Sends a    │──────> │  │ Defines:    │──>│ Functions that  │  │
  │  Query or   │  POST  │  │ - Types     │   │ fetch actual    │  │
  │  Mutation   │        │  │ - Fields    │   │ data from:      │  │
  │             │        │  │ - Relations │   │ - Database      │  │
  │             │<────── │  └─────────────┘   │ - Other APIs    │  │
  │  Receives   │  JSON  │                     │ - Files         │  │
  │  exactly    │        │  ┌─────────────┐   └────────┬────────┘  │
  │  what it    │        │  │  VALIDATOR  │            │           │
  │  asked for  │        │  │             │            ▼           │
  │             │        │  │ Checks if   │   ┌─────────────────┐  │
  └─────────────┘        │  │ query is    │   │  DATA SOURCES   │  │
                         │  │ valid per   │   │                 │  │
                         │  │ schema      │   │  ┌───────────┐  │  │
                         │  └─────────────┘   │  │ Database  │  │  │
                         │                     │  └───────────┘  │  │
                         │                     │  ┌───────────┐  │  │
                         │                     │  │ REST APIs │  │  │
                         │                     │  └───────────┘  │  │
                         │                     └─────────────────┘  │
                         └──────────────────────────────────────────┘

════════════════════════════════════════════════════════════════════════
  Step 1: Client sends a GraphQL query in the HTTP request body
  Step 2: Server parses and validates the query against the Schema
  Step 3: Resolvers fetch the requested data from data sources
  Step 4: Server returns a JSON response with exactly the data asked
════════════════════════════════════════════════════════════════════════
```

---

## 4. Core GraphQL Terminologies

> 📌 Learn these terms first — they are the foundation of everything in GraphQL.

---

### 4.1 🗂️ Schema

**Definition:** The **Schema** is like a contract or blueprint of your entire API. It defines:
- What **types** of data exist
- What **fields** each type has
- What **queries** (reading) and **mutations** (writing) are allowed
- What **relationships** exist between types

**Analogy:** Think of a Schema like the **menu of a restaurant** — it tells you everything you CAN order, but not what you WILL order.

```graphql
# Example Schema
type User {
  id: ID!
  name: String!
  email: String!
  age: Int
  posts: [Post!]!
}

type Post {
  id: ID!
  title: String!
  content: String
  author: User!
}
```

---

### 4.2 📦 Types

**Definition:** Types are the building blocks of GraphQL Schema. They describe the shape of data.

**There are 2 main categories:**

#### A) Scalar Types (built-in primitive types)

| Scalar Type | Description | Example Value |
|---|---|---|
| `String` | Text data | `"Alice"` |
| `Int` | Whole numbers | `25` |
| `Float` | Decimal numbers | `9.99` |
| `Boolean` | True or False | `true` |
| `ID` | Unique identifier (string internally) | `"user_123"` |

#### B) Object Types (custom types you define)

```graphql
# This is an Object Type named "Product"
type Product {
  id: ID!           # ID scalar — must not be null (!)
  name: String!     # String scalar — required
  price: Float!     # Float scalar — required
  inStock: Boolean  # Boolean scalar — optional (no !)
  category: String  # String scalar — optional
}
```

---

### 4.3 ❗ Non-Null Modifier (`!`)

**Definition:** The exclamation mark `!` means the field is **required** and will **never return null**.

```graphql
type User {
  id: ID!       # ✅ REQUIRED — will always have a value
  name: String! # ✅ REQUIRED — will always have a value
  bio: String   # ⚠️ OPTIONAL — can be null
}
```

---

### 4.4 📋 Lists (`[ ]`)

**Definition:** Square brackets define a **list (array)** of a type.

```graphql
type Author {
  books: [Book]    # List of books — list can be null, books can be null
  tags: [String!]  # List of strings — each string must not be null
  posts: [Post!]!  # List must exist AND each Post must exist
}
```

---

### 4.5 🔍 Query

**Definition:** A **Query** is used to **read/fetch** data from the GraphQL server. It is equivalent to `GET` in REST.

```graphql
# The Query type defines all available "read" operations
type Query {
  user(id: ID!): User
  users: [User!]!
  product(id: ID!): Product
  searchProducts(keyword: String!): [Product]
}
```

---

### 4.6 ✏️ Mutation

**Definition:** A **Mutation** is used to **create, update, or delete** data. It is equivalent to `POST`, `PUT`, `PATCH`, `DELETE` in REST.

```graphql
# The Mutation type defines all available "write" operations
type Mutation {
  createUser(name: String!, email: String!): User
  updateUser(id: ID!, name: String): User
  deleteUser(id: ID!): Boolean
}
```

---

### 4.7 📡 Subscription

**Definition:** A **Subscription** is used to receive **real-time data updates** via a persistent connection (WebSocket). When data changes on the server, subscribed clients get notified automatically.

```graphql
type Subscription {
  messageAdded(chatRoomId: ID!): Message
  orderStatusChanged(orderId: ID!): Order
}
```

---

### 4.8 🏗️ Resolver

**Definition:** A **Resolver** is a **function on the server side** that knows how to fetch the data for a particular field. Every field in GraphQL has a resolver (though many are auto-generated).

**Analogy:** If the Schema is the menu, then Resolvers are the **kitchen staff** who actually prepare your order.

```javascript
// Example Resolver (Node.js / JavaScript)
const resolvers = {
  Query: {
    // This resolver handles the "user" query
    user: (parent, args, context) => {
      return database.findUserById(args.id); // fetch from DB
    },
    // This resolver handles the "users" query
    users: () => {
      return database.getAllUsers();
    }
  }
};
```

---

### 4.9 📝 Field

**Definition:** A **Field** is a single piece of data inside a Type. Think of fields like columns in a database table.

```graphql
type Movie {
  id: ID!          # ← "id" is a field
  title: String!   # ← "title" is a field
  year: Int        # ← "year" is a field
  rating: Float    # ← "rating" is a field
}
```

---

### 4.10 📌 Arguments

**Definition:** **Arguments** are parameters you can pass to a field to filter, sort, or customize the data you get back.

```graphql
# In the Schema, arguments are defined like this:
type Query {
  users(limit: Int, offset: Int, sortBy: String): [User]
  user(id: ID!): User
}

# In a Query, you pass arguments like this:
query {
  users(limit: 10, sortBy: "name") {
    name
    email
  }
}
```

---

### 4.11 📂 Fragments

**Definition:** A **Fragment** is a reusable piece of a query. It lets you define a set of fields once and use them in multiple queries — keeping your queries DRY (Don't Repeat Yourself).

```graphql
# Define a fragment on the User type
fragment UserBasicInfo on User {
  id
  name
  email
}

# Use the fragment in queries
query {
  user(id: "1") {
    ...UserBasicInfo   # ← spread the fragment here
    posts {
      title
    }
  }
}
```

---

### 4.12 🔄 Aliases

**Definition:** **Aliases** let you rename a field in the response, or make multiple calls to the same field with different arguments in one query.

```graphql
query {
  # Alias "firstUser" and "secondUser" for the same "user" field
  firstUser: user(id: "1") {
    name
    email
  }
  secondUser: user(id: "2") {
    name
    email
  }
}

# Response:
# {
#   "data": {
#     "firstUser": { "name": "Alice", "email": "alice@example.com" },
#     "secondUser": { "name": "Bob", "email": "bob@example.com" }
#   }
# }
```

---

### 4.13 📋 Variables

**Definition:** **Variables** allow you to pass dynamic values to your queries and mutations. Instead of hardcoding values, you define placeholders with `$`.

```graphql
# Query with variables ($ prefix marks a variable)
query GetUser($userId: ID!) {
  user(id: $userId) {
    name
    email
  }
}

# Variables are passed separately as JSON:
{
  "userId": "42"
}
```

---

### 4.14 🔍 Directives

**Definition:** **Directives** tell GraphQL to include or skip fields based on conditions.

```graphql
query GetUser($showEmail: Boolean!, $skipBio: Boolean!) {
  user(id: "1") {
    name
    email @include(if: $showEmail)  # Include email only if showEmail is true
    bio @skip(if: $skipBio)         # Skip bio if skipBio is true
  }
}
```

---

### 4.15 🔎 Introspection

**Definition:** **Introspection** is GraphQL's built-in ability to query its own schema. It lets you discover what types, fields, and operations are available — this is how GraphQL tools auto-generate documentation.

```graphql
# Ask the server: what types exist?
query {
  __schema {
    types {
      name
      kind
    }
  }
}

# Ask the server: what fields does the User type have?
query {
  __type(name: "User") {
    fields {
      name
      type {
        name
      }
    }
  }
}
```

---

### 4.16 🔗 Interfaces

**Definition:** An **Interface** defines a set of fields that multiple types must include — like an abstract class in OOP.

```graphql
interface Animal {
  id: ID!
  name: String!
  sound: String!
}

type Dog implements Animal {
  id: ID!
  name: String!
  sound: String!
  breed: String  # Extra field specific to Dog
}

type Cat implements Animal {
  id: ID!
  name: String!
  sound: String!
  indoor: Boolean  # Extra field specific to Cat
}
```

---

### 4.17 🔀 Union Types

**Definition:** A **Union** type means a field can return one of several different object types.

```graphql
union SearchResult = User | Post | Product

type Query {
  search(keyword: String!): [SearchResult]
}

# When querying a union, use inline fragments:
query {
  search(keyword: "alice") {
    ... on User {
      name
      email
    }
    ... on Post {
      title
    }
    ... on Product {
      name
      price
    }
  }
}
```

---

### 4.18 📥 Input Types

**Definition:** **Input Types** are special object types used to pass complex objects as arguments (especially in mutations).

```graphql
# Define an Input Type
input CreateUserInput {
  name: String!
  email: String!
  age: Int
  address: AddressInput
}

input AddressInput {
  street: String!
  city: String!
  zipCode: String!
}

# Use the Input Type in a Mutation
type Mutation {
  createUser(input: CreateUserInput!): User
}
```

---

## 5. GraphQL Schema — The Blueprint

### 📐 Full Schema Structure Diagram

```
════════════════════════════════════════════════════════════════════════
                  GRAPHQL SCHEMA ANATOMY
════════════════════════════════════════════════════════════════════════

  schema {
    query:    Query        ← Entry point for READ operations
    mutation: Mutation     ← Entry point for WRITE operations
    subscription: Subscription  ← Entry point for REAL-TIME
  }
         │                      │                    │
         ▼                      ▼                    ▼
  ┌─────────────┐      ┌─────────────┐     ┌─────────────────┐
  │  type Query │      │type Mutation│     │type Subscription│
  │─────────────│      │─────────────│     │─────────────────│
  │ user(id)    │      │ createUser  │     │ messageAdded    │
  │ users()     │      │ updateUser  │     │ orderUpdated    │
  │ products()  │      │ deleteUser  │     └─────────────────┘
  └──────┬──────┘      │ createPost  │
         │              └─────────────┘
         │
         ▼
  ┌─────────────────────────────────────────┐
  │            OBJECT TYPES                 │
  │                                         │
  │  type User          type Post           │
  │  ──────────         ──────────          │
  │  id: ID!            id: ID!             │
  │  name: String!      title: String!      │
  │  email: String!     content: String     │
  │  posts: [Post!]! ──>author: User!       │
  │  age: Int           createdAt: String   │
  └─────────────────────────────────────────┘
         │
         ▼
  ┌─────────────────────────────────────────┐
  │           SCALAR TYPES                  │
  │  String  Int  Float  Boolean  ID        │
  │  (custom scalars also possible)         │
  └─────────────────────────────────────────┘

════════════════════════════════════════════════════════════════════════
```

### 📝 Complete Example Schema

```graphql
# ─────────────────────────────────────────────────
# SCALAR TYPES (built-in, no definition needed)
# String, Int, Float, Boolean, ID
# ─────────────────────────────────────────────────

# ─────────────────────────────────────────────────
# OBJECT TYPES
# ─────────────────────────────────────────────────

type User {
  id: ID!
  name: String!
  email: String!
  age: Int
  role: Role!           # Using an Enum type
  posts: [Post!]!       # Relationship to Post
  createdAt: String!
}

type Post {
  id: ID!
  title: String!
  content: String!
  published: Boolean!
  author: User!         # Relationship back to User
  comments: [Comment!]!
  tags: [String!]!
}

type Comment {
  id: ID!
  text: String!
  author: User!
  post: Post!
}

# ─────────────────────────────────────────────────
# ENUM TYPE
# ─────────────────────────────────────────────────

enum Role {
  ADMIN
  EDITOR
  VIEWER
}

# ─────────────────────────────────────────────────
# INPUT TYPES (for Mutations)
# ─────────────────────────────────────────────────

input CreateUserInput {
  name: String!
  email: String!
  age: Int
  role: Role
}

input UpdateUserInput {
  name: String
  email: String
  age: Int
}

input CreatePostInput {
  title: String!
  content: String!
  authorId: ID!
  tags: [String!]
}

# ─────────────────────────────────────────────────
# QUERY TYPE (Read operations)
# ─────────────────────────────────────────────────

type Query {
  # Get a single user by ID
  user(id: ID!): User

  # Get all users (with optional filters)
  users(limit: Int, offset: Int): [User!]!

  # Search users by name
  searchUsers(name: String!): [User]

  # Get a single post
  post(id: ID!): Post

  # Get all published posts
  posts(published: Boolean): [Post!]!
}

# ─────────────────────────────────────────────────
# MUTATION TYPE (Write operations)
# ─────────────────────────────────────────────────

type Mutation {
  # Create a new user
  createUser(input: CreateUserInput!): User!

  # Update an existing user
  updateUser(id: ID!, input: UpdateUserInput!): User

  # Delete a user (returns true if successful)
  deleteUser(id: ID!): Boolean!

  # Create a new post
  createPost(input: CreatePostInput!): Post!

  # Publish or unpublish a post
  publishPost(id: ID!, published: Boolean!): Post
}

# ─────────────────────────────────────────────────
# SUBSCRIPTION TYPE (Real-time)
# ─────────────────────────────────────────────────

type Subscription {
  # Fires when a new comment is added to a post
  commentAdded(postId: ID!): Comment!
}
```

---

## 6. GraphQL Queries — Fetching Data

### 🔍 How a Query Works

```
════════════════════════════════════════════════════════════════════════
                    QUERY FLOW
════════════════════════════════════════════════════════════════════════

  1. You write a Query          2. Server validates it
  ┌─────────────────────┐      ┌─────────────────────┐
  │ query {             │      │ Is "user" defined   │
  │   user(id: "1") {  │─────>│ in the Schema?      │
  │     name            │      │ Is "name" a valid   │
  │     email           │      │ field of User?      │
  │   }                 │      └──────────┬──────────┘
  └─────────────────────┘                 │
                                          │ Yes ✅
                                          ▼
                              3. Resolver runs
                              ┌─────────────────────┐
                              │ user(id) {          │
                              │   return DB.find(id)│
                              │ }                   │
                              └──────────┬──────────┘
                                         │
                                         ▼
                              4. Response returned
                              ┌─────────────────────┐
                              │ {                   │
                              │   "data": {         │
                              │     "user": {       │
                              │       "name": "Bob",│
                              │       "email": ".." │
                              │     }               │
                              │   }                 │
                              │ }                   │
                              └─────────────────────┘
════════════════════════════════════════════════════════════════════════
```

---

### 📝 Query Examples (From Simple to Complex)

#### Example 1: Simple Query — Get a user by ID

```graphql
query {
  user(id: "1") {
    id
    name
    email
  }
}
```

**Response:**
```json
{
  "data": {
    "user": {
      "id": "1",
      "name": "Alice Johnson",
      "email": "alice@example.com"
    }
  }
}
```

---

#### Example 2: Nested Query — Get user with their posts

```graphql
query {
  user(id: "1") {
    name
    email
    posts {
      title
      published
      comments {
        text
      }
    }
  }
}
```

**Response:**
```json
{
  "data": {
    "user": {
      "name": "Alice Johnson",
      "email": "alice@example.com",
      "posts": [
        {
          "title": "My First Post",
          "published": true,
          "comments": [
            { "text": "Great post!" },
            { "text": "Very helpful, thanks!" }
          ]
        }
      ]
    }
  }
}
```

---

#### Example 3: Named Query (Best Practice)

```graphql
# Name your queries — makes debugging and tracking easier
query GetUserProfile {
  user(id: "1") {
    name
    email
    role
  }
}
```

---

#### Example 4: Query with Variables

```graphql
# Query definition with variable
query GetUser($id: ID!, $showRole: Boolean!) {
  user(id: $id) {
    name
    email
    role @include(if: $showRole)
  }
}
```

**Variables (sent alongside the query):**
```json
{
  "id": "42",
  "showRole": true
}
```

---

#### Example 5: Query with Aliases (Multiple queries at once)

```graphql
query GetTwoUsers {
  alice: user(id: "1") {
    name
    email
  }
  bob: user(id: "2") {
    name
    email
  }
}
```

**Response:**
```json
{
  "data": {
    "alice": {
      "name": "Alice",
      "email": "alice@example.com"
    },
    "bob": {
      "name": "Bob",
      "email": "bob@example.com"
    }
  }
}
```

---

#### Example 6: Query with Fragments (Reusable fields)

```graphql
# Define a fragment
fragment PostDetails on Post {
  id
  title
  published
  author {
    name
  }
}

# Use the fragment
query GetAllPosts {
  posts {
    ...PostDetails
    comments {
      text
    }
  }
}
```

---

## 7. GraphQL Mutations — Modifying Data

### ✏️ What is a Mutation?

Mutations are the equivalent of **POST, PUT, PATCH, DELETE** in REST. They change data on the server.

```
════════════════════════════════════════════════════════════════════════
          MUTATION TYPES AND THEIR REST EQUIVALENTS
════════════════════════════════════════════════════════════════════════

  CREATE   ≈   POST /users
  ┌──────────────────────────────────────────────────────────────────┐
  │  mutation {                                                      │
  │    createUser(input: { name: "Alice", email: "a@b.com" }) {     │
  │      id                                                          │
  │      name                                                        │
  │    }                                                             │
  │  }                                                               │
  └──────────────────────────────────────────────────────────────────┘

  UPDATE   ≈   PUT/PATCH /users/:id
  ┌──────────────────────────────────────────────────────────────────┐
  │  mutation {                                                      │
  │    updateUser(id: "1", input: { name: "Alice Smith" }) {        │
  │      id                                                          │
  │      name                                                        │
  │    }                                                             │
  │  }                                                               │
  └──────────────────────────────────────────────────────────────────┘

  DELETE   ≈   DELETE /users/:id
  ┌──────────────────────────────────────────────────────────────────┐
  │  mutation {                                                      │
  │    deleteUser(id: "1")                                           │
  │  }                                                               │
  └──────────────────────────────────────────────────────────────────┘
════════════════════════════════════════════════════════════════════════
```

---

### 📝 Mutation Examples

#### Example 1: Create a new user

```graphql
mutation CreateNewUser {
  createUser(input: {
    name: "John Doe"
    email: "john@example.com"
    age: 28
    role: EDITOR
  }) {
    id
    name
    email
    createdAt
  }
}
```

**Response:**
```json
{
  "data": {
    "createUser": {
      "id": "101",
      "name": "John Doe",
      "email": "john@example.com",
      "createdAt": "2026-04-12T10:30:00Z"
    }
  }
}
```

---

#### Example 2: Update a user

```graphql
mutation UpdateUserEmail {
  updateUser(id: "101", input: {
    email: "john.doe@newdomain.com"
  }) {
    id
    name
    email
  }
}
```

**Response:**
```json
{
  "data": {
    "updateUser": {
      "id": "101",
      "name": "John Doe",
      "email": "john.doe@newdomain.com"
    }
  }
}
```

---

#### Example 3: Delete a user

```graphql
mutation RemoveUser {
  deleteUser(id: "101")
}
```

**Response:**
```json
{
  "data": {
    "deleteUser": true
  }
}
```

---

#### Example 4: Mutation with Variables (Best Practice)

```graphql
mutation CreateUser($input: CreateUserInput!) {
  createUser(input: $input) {
    id
    name
    email
  }
}
```

**Variables:**
```json
{
  "input": {
    "name": "Sarah Connor",
    "email": "sarah@skynet.com",
    "age": 35,
    "role": "ADMIN"
  }
}
```

---

## 8. GraphQL Subscriptions — Real-time Data

### 📡 When to Use Subscriptions

Use subscriptions for **live, real-time updates** such as:
- Chat applications (new messages)
- Live notifications
- Order tracking
- Live sports scores
- Stock price updates

```graphql
# Subscribe to new comments on a post
subscription OnNewComment {
  commentAdded(postId: "42") {
    id
    text
    author {
      name
    }
  }
}
```

**How it works:**
```
Client ──── WebSocket Connection ───> Server

Server ─── Pushes update automatically when data changes ──> Client
```

---

## 9. Testing GraphQL — Step-by-Step Guide

### 🧪 Tools for Testing GraphQL

| Tool | Type | Best For |
|---|---|---|
| **GraphQL Playground** | Browser-based IDE | Quick manual testing |
| **GraphiQL** | Browser-based IDE | Built-in to many GraphQL servers |
| **Postman** | Desktop/Web App | API testing — supports GraphQL |
| **Insomnia** | Desktop App | Developer-friendly REST + GraphQL testing |
| **Bruno** | Desktop App | Open-source, file-based API testing |
| **curl** | Command Line | Scripting and automation |
| **Apollo Studio** | Web-based | Enterprise-level GraphQL management |

---

### 📋 Step-by-Step: Testing GraphQL Queries with GraphQL Playground / GraphiQL

#### ✅ Step 1: Open GraphQL Playground

Most GraphQL APIs expose a playground at:
```
http://localhost:4000/graphql
http://your-api.com/graphql
```

You'll see a **split-panel interface**:
- **Left panel** → Write your Query/Mutation
- **Right panel** → See the Response
- **Bottom panel** → Variables and Headers

---

#### ✅ Step 2: Explore the Schema (Introspection)

Before writing queries, understand what's available:

1. Click the **"Docs"** or **"Schema"** tab on the right side
2. Browse available Queries, Mutations, and Types
3. Or run an introspection query:

```graphql
# See all types in the schema
query {
  __schema {
    queryType { name }
    mutationType { name }
    types {
      name
      kind
      description
    }
  }
}
```

---

#### ✅ Step 3: Write and Run a Simple Query

```graphql
query {
  users {
    id
    name
    email
  }
}
```

1. Type (or paste) the query in the left panel
2. Press the **▶ Play button** (or Ctrl+Enter)
3. Check the right panel for the response

**What to verify as a QA Tester:**
- [ ] Status is `200 OK` (GraphQL almost always returns 200)
- [ ] Response has a `"data"` key
- [ ] No `"errors"` key in the response
- [ ] All requested fields are present in the response
- [ ] Data types match schema (string is string, not a number)
- [ ] Relationships are correctly populated

---

#### ✅ Step 4: Test with Variables

In the Playground's **Variables panel** (bottom-left):

**Query:**
```graphql
query GetUserById($id: ID!) {
  user(id: $id) {
    id
    name
    email
    role
    posts {
      title
    }
  }
}
```

**Variables panel:**
```json
{
  "id": "1"
}
```

**Press play and verify the response.**

---

#### ✅ Step 5: Test Query Edge Cases (QA Checklist)

```graphql
# Test 1: Request a valid user
query { user(id: "1") { name } }
# ✅ Expected: Returns user data

# Test 2: Request a non-existent user
query { user(id: "99999") { name } }
# ✅ Expected: Returns null or error — NOT a 404 HTTP error!
# GraphQL returns: { "data": { "user": null } }

# Test 3: Request with missing required argument
query { user { name } }
# ✅ Expected: Validation error — id is required
# GraphQL returns: { "errors": [...] }

# Test 4: Request a non-existent field
query { user(id: "1") { nonExistentField } }
# ✅ Expected: Validation error (field doesn't exist in schema)
```

---

#### ✅ Step 6: Testing GraphQL with Postman

1. Open Postman and create a new request
2. Set method to **POST**
3. Set URL to `http://your-api.com/graphql`
4. Go to **Body** tab → Select **GraphQL**
5. In the **Query** field, paste:
   ```graphql
   query GetUsers {
     users {
       id
       name
       email
     }
   }
   ```
6. In the **Variables** field (below query):
   ```json
   {}
   ```
7. Add headers if needed:
   - `Content-Type: application/json`
   - `Authorization: Bearer YOUR_TOKEN`
8. Click **Send**

---

#### ✅ Step 7: Testing GraphQL with curl (Command Line)

```bash
# Basic Query
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"query":"{ users { id name email } }"}' \
  http://localhost:4000/graphql

# Query with variables
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{
    "query": "query GetUser($id: ID!) { user(id: $id) { name email } }",
    "variables": {"id": "1"}
  }' \
  http://localhost:4000/graphql

# With Authorization header
curl -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGc..." \
  -d '{"query":"{ me { name email } }"}' \
  http://localhost:4000/graphql
```

---

### 🧪 QA Test Cases for GraphQL Queries

```
════════════════════════════════════════════════════════════════════════
              QA TEST CASE CHECKLIST — QUERIES
════════════════════════════════════════════════════════════════════════

✅ HAPPY PATH TESTS
─────────────────────────────────────────────────────────────────────
□ Query returns correct data for valid input
□ All requested fields are present in response
□ Nested objects are correctly returned
□ List queries return arrays
□ Pagination (limit/offset) works correctly
□ Sorting works correctly

✅ NEGATIVE/EDGE CASE TESTS
─────────────────────────────────────────────────────────────────────
□ Query with non-existent ID returns null (not 500)
□ Query with missing required argument returns validation error
□ Query with wrong argument type returns error
□ Query with unknown field returns validation error
□ Empty list returns [] not null
□ Deeply nested query (N+1 problem check)

✅ AUTHENTICATION TESTS
─────────────────────────────────────────────────────────────────────
□ Protected query without auth token returns error
□ Protected query with invalid token returns error
□ Protected query with valid token returns data
□ User can only access their own data (authorization)

✅ PERFORMANCE TESTS
─────────────────────────────────────────────────────────────────────
□ Query response time is within acceptable limits
□ Large dataset query doesn't time out
□ Pagination prevents loading too much data
════════════════════════════════════════════════════════════════════════
```

---

## 10. Testing Mutations — Step-by-Step Guide

### ✅ Step 1: Test a CREATE Mutation

```graphql
mutation CreateUser {
  createUser(input: {
    name: "Test User"
    email: "testuser@example.com"
    age: 25
    role: VIEWER
  }) {
    id
    name
    email
    role
    createdAt
  }
}
```

**What to verify as a QA Tester:**
- [ ] Response status is `200 OK`
- [ ] `"data"` is present, `"errors"` is absent
- [ ] Returned `id` is a valid, unique ID
- [ ] All input fields are reflected correctly in the response
- [ ] `createdAt` timestamp is populated automatically
- [ ] Run a subsequent `GET` (Query) to confirm the record was actually saved

---

#### ✅ Step 2: Test an UPDATE Mutation

```graphql
# First, note the ID from the create response (e.g., "id": "55")

mutation UpdateUser {
  updateUser(id: "55", input: {
    name: "Updated Name"
    email: "updated@example.com"
  }) {
    id
    name
    email
  }
}
```

**What to verify:**
- [ ] Updated fields reflect new values
- [ ] Non-updated fields remain unchanged
- [ ] Run a Query after to confirm persistence of changes
- [ ] Try updating a non-existent ID → should return null or error

---

#### ✅ Step 3: Test a DELETE Mutation

```graphql
mutation DeleteUser {
  deleteUser(id: "55")
}
```

**What to verify:**
- [ ] Response returns `true` on success
- [ ] Run a Query with the deleted ID → should return `null`
- [ ] Try deleting the same ID again → should return `false` or error
- [ ] Try deleting a non-existent ID → should return `false` or error

---

#### ✅ Step 4: Test Mutations with Variables (Recommended Practice)

```graphql
mutation CreateUser($input: CreateUserInput!) {
  createUser(input: $input) {
    id
    name
    email
  }
}
```

**Variables:**
```json
{
  "input": {
    "name": "Variable Test User",
    "email": "vartest@example.com",
    "role": "EDITOR"
  }
}
```

---

#### ✅ Step 5: Test Mutation Validation

```graphql
# Test 1: Missing required field (name is required)
mutation {
  createUser(input: {
    email: "missing@name.com"
  }) { id }
}
# ✅ Expected: Validation error "name is required"

# Test 2: Invalid email format (if validated on server)
mutation {
  createUser(input: {
    name: "Test"
    email: "not-an-email"
  }) { id }
}
# ✅ Expected: Validation error for email format

# Test 3: Duplicate email (if unique constraint exists)
mutation {
  createUser(input: {
    name: "Test"
    email: "alice@example.com"  # already exists
  }) { id }
}
# ✅ Expected: Business logic error about duplicate email
```

---

### 🧪 QA Test Case Checklist — Mutations

```
════════════════════════════════════════════════════════════════════════
              QA TEST CASE CHECKLIST — MUTATIONS
════════════════════════════════════════════════════════════════════════

✅ CREATE MUTATION TESTS
─────────────────────────────────────────────────────────────────────
□ Valid input creates record and returns correct data
□ All required fields validated — missing field returns error
□ Unique constraints enforced (e.g., duplicate email)
□ Data types validated (e.g., age must be Int, not String)
□ Verify created record persists by running a follow-up Query
□ Auto-generated fields (id, createdAt) are present in response

✅ UPDATE MUTATION TESTS
─────────────────────────────────────────────────────────────────────
□ Valid update changes only specified fields
□ Unspecified fields remain unchanged
□ Update with non-existent ID returns null/error (not a crash)
□ Validate updated data with a follow-up Query
□ Partial updates work correctly (only email changes, name stays)

✅ DELETE MUTATION TESTS
─────────────────────────────────────────────────────────────────────
□ Delete valid ID returns success (true)
□ Verify deleted record is gone with a follow-up Query
□ Delete already-deleted ID returns failure (false/error)
□ Delete non-existent ID handled gracefully
□ Cascade deletes work (e.g., deleting user deletes their posts)

✅ AUTHORIZATION TESTS (ALL MUTATIONS)
─────────────────────────────────────────────────────────────────────
□ Unauthenticated request is rejected
□ User cannot update/delete another user's data
□ Admin can perform operations a regular user cannot
□ Invalid token is rejected properly

✅ CONCURRENT/EDGE CASE TESTS
─────────────────────────────────────────────────────────────────────
□ Race condition: Create same record twice simultaneously
□ Very long string values handled (e.g., 10,000 char name)
□ Special characters in strings (Unicode, emojis, SQL injection)
□ Numeric boundary values (Int max/min)
════════════════════════════════════════════════════════════════════════
```

---

### 📋 Testing Mutations with Postman — Step-by-Step

1. **Open Postman** → New Request
2. **Method:** POST
3. **URL:** `http://your-api.com/graphql`
4. **Headers tab:**
   ```
   Content-Type: application/json
   Authorization: Bearer <your-token>
   ```
5. **Body tab** → Select **GraphQL**
6. Paste in the **Query field:**
   ```graphql
   mutation CreateUser($input: CreateUserInput!) {
     createUser(input: $input) {
       id
       name
       email
     }
   }
   ```
7. Paste in the **Variables field:**
   ```json
   {
     "input": {
       "name": "Postman Test User",
       "email": "postman@test.com"
     }
   }
   ```
8. Click **Send**
9. **Verify the response:**
   - HTTP Status: `200 OK`
   - Body has `"data"` key
   - No `"errors"` key
   - Returned data matches input

---

### 🔄 Understanding GraphQL Error Responses

> ⚠️ **Key Difference from REST:** GraphQL ALWAYS returns HTTP 200. Errors are inside the response body!

#### Error Response Structure:

```json
{
  "data": null,
  "errors": [
    {
      "message": "User with id '999' not found",
      "locations": [{ "line": 2, "column": 3 }],
      "path": ["user"],
      "extensions": {
        "code": "NOT_FOUND",
        "statusCode": 404
      }
    }
  ]
}
```

#### Partial Success (some fields work, some fail):

```json
{
  "data": {
    "user": {
      "name": "Alice",
      "secretField": null
    }
  },
  "errors": [
    {
      "message": "Not authorized to view secretField",
      "path": ["user", "secretField"]
    }
  ]
}
```

---

## 11. Common Errors & How to Debug

| Error Message | Cause | Fix |
|---|---|---|
| `Cannot query field "X" on type "Y"` | Field doesn't exist in schema | Check the schema docs for correct field name |
| `Field "X" argument "Y" of type "Z!" is required` | Missing required argument | Add the required argument to your query |
| `Variable "$X" of type "Y" used in position expecting type "Z"` | Wrong variable type | Match variable type to schema definition |
| `Syntax Error: Expected Name, found )` | Missing field inside selection set | Add at least one field inside `{ }` |
| `Must provide query string` | Empty body sent | Ensure query body is not empty |
| `Cannot return null for non-nullable field` | Server-side resolver returned null for a `!` field | Server-side bug — report to developer |
| `401 Unauthorized` in errors | Missing or invalid auth token | Add valid Bearer token to headers |

---

### 🔍 Debugging Tips

```
1. ALWAYS check the "errors" array in the response body
   (even if HTTP status is 200!)

2. Use __typename in your queries to confirm object types:
   query { user(id: "1") { __typename name } }
   Response: { "user": { "__typename": "User", "name": "Alice" } }

3. Use Introspection to verify field names and types:
   query { __type(name: "User") { fields { name type { name } } } }

4. Check the "path" in errors to know which field failed

5. Check the "locations" in errors to find where in your query the error is
```

---

## 12. Quick Reference Cheat Sheet

```
════════════════════════════════════════════════════════════════════════
                  GRAPHQL CHEAT SHEET
════════════════════════════════════════════════════════════════════════

OPERATION TYPES
───────────────────────────────────────────────────────────────────────
query     → Read data (like GET in REST)
mutation  → Write data (like POST/PUT/DELETE in REST)
subscription → Real-time data (like WebSockets in REST)

SCHEMA KEYWORDS
───────────────────────────────────────────────────────────────────────
type      → Define an object type
scalar    → Primitive types (String, Int, Float, Boolean, ID)
enum      → Fixed set of values
interface → Abstract type (must be implemented by other types)
union     → Field can be one of multiple types
input     → Object type used as argument in mutations

MODIFIERS
───────────────────────────────────────────────────────────────────────
!         → Non-null (required, can never be null)
[ ]       → List/Array
[Type!]!  → Non-null list of non-null items

QUERY FEATURES
───────────────────────────────────────────────────────────────────────
$varName  → Variable declaration (e.g., $id: ID!)
...fragment → Spread a fragment
alias:    → Rename a field in the response
@include(if: $bool) → Conditionally include a field
@skip(if: $bool)    → Conditionally skip a field

INTROSPECTION
───────────────────────────────────────────────────────────────────────
__schema  → Info about the whole schema
__type    → Info about a specific type
__typename→ Get the type name of an object

RESPONSE STRUCTURE
───────────────────────────────────────────────────────────────────────
{
  "data":   { ... }     ← Successful result
  "errors": [ ... ]     ← Errors (can exist alongside data)
}

HTTP
───────────────────────────────────────────────────────────────────────
Always POST to a single endpoint  /graphql
Content-Type: application/json
Body: { "query": "...", "variables": {...}, "operationName": "..." }
Response status is always 200 (check body for errors!)

════════════════════════════════════════════════════════════════════════
```

---

## 🎓 Summary

```
    GraphQL in a Nutshell:
    ┌────────────────────────────────────────────────────┐
    │  • Single endpoint for all operations              │
    │  • Client requests exactly the data it needs       │
    │  • Schema defines what's possible                  │
    │  • Queries = Read, Mutations = Write               │
    │  • Subscriptions = Real-time                       │
    │  • Always returns HTTP 200 — check body for errors │
    │  • Built-in introspection for self-documentation   │
    └────────────────────────────────────────────────────┘
```

---

> 📌 **Next Steps to Practice:**
> 1. Try the free GraphQL playground at [https://countries.trevorblades.com/](https://countries.trevorblades.com/)
> 2. Explore Star Wars GraphQL API at [https://swapi-graphql.netlify.app/](https://swapi-graphql.netlify.app/)
> 3. Explore official GraphQL docs at [https://graphql.org/learn/](https://graphql.org/learn/)

---

*Created for QA Testers & Developers | Covers GraphQL Fundamentals, Schema Design, Queries, Mutations, and Testing*
