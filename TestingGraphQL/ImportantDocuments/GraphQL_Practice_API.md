# 🚀 Best Free GraphQL Demo APIs

## 1. 🧪 GraphQL Zero (BEST for beginners)

* 🌐 Endpoint: `https://graphqlzero.almansi.me/api`
* 📌 Entity: GraphQL Zero
* ✅ No auth required
* ✅ CRUD operations (queries + mutations)
* ✅ Based on JSONPlaceholder data

👉 Why it’s great:

* Perfect for **Postman + Rest Assured**
* Supports **create, update, delete (mutations)**
* Realistic data like users, posts, comments

📌 Example query:

```graphql
{
  posts(options: {paginate: {page: 1, limit: 5}}) {
    data {
      id
      title
    }
  }
}
```

➡️ It’s specifically built for **testing & prototyping GraphQL apps** ([graphqlzero.almansi.me][1])

---

## 2. 🌍 Countries / GeoGraphQL API

* 🌐 Endpoint: `https://geographql.netlify.app`
* 📌 Entity: GeoGraphQL
* ✅ No auth
* ✅ Nested queries (country → state → city)

👉 Best for:

* Practicing **nested GraphQL queries**
* Understanding **relations**

📌 Example:

```graphql
{
  country(code: "IN") {
    name
    states {
      name
    }
  }
}
```

➡️ Fully public API with country/state/city data ([openpublicapis.com][2])

---

## 3. 🚀 SpaceX GraphQL API

* 🌐 Endpoint: `https://api.spacex.land/graphql/`
* 📌 Entity: SpaceX GraphQL API
* ✅ No auth
* ✅ Real-world production-like data

👉 Best for:

* Complex queries
* Automation + performance testing

📌 Example:

```graphql
{
  launchesPast(limit: 3) {
    mission_name
    launch_date_local
  }
}
```

➡️ Widely used in demos and tutorials ([postman.com][3])

---

## 4. 🌌 SWAPI GraphQL (Star Wars API)

* 🌐 Endpoint: `https://swapi-graphql.netlify.app/.netlify/functions/index`
* 📌 Entity: SWAPI GraphQL
* ✅ No auth

👉 Best for:

* Practicing **relationships (films, people, planets)**
* Interview demos

---

## 5. 🧬 Rick & Morty GraphQL API

* 📌 Entity: Rick and Morty GraphQL API
* ✅ Public + fun dataset

👉 Good for:

* Filtering, pagination
* Response validation in Rest Assured

---

## 6. 🌐 Countries GraphQL (Simple & fast)

* Endpoint: `https://countries.trevorblades.com/`
* 📌 Entity: Countries GraphQL API
* ✅ No auth
* ✅ Lightweight

👉 Best for:

* Beginners
* Quick Postman demos

---

## 7. 📦 Postman Public GraphQL Collection

* 📌 Entity: Postman GraphQL Examples Workspace

👉 Why useful:

* Ready-made queries
* Multiple APIs in one place
* Can fork and run instantly

➡️ Contains **100+ public GraphQL APIs** for testing ([postman.com][4])

---

# 🔥 Bonus (Advanced)

## GitHub GraphQL API

* Endpoint: `https://api.github.com/graphql`
* ⚠️ Requires token

👉 Best for:

* Real enterprise-level testing
* Complex mutation scenarios

---

# 💡 How to Use in Postman / Rest Assured

### 📌 Postman

1. Select **POST**
2. URL = GraphQL endpoint
3. Body → GraphQL → paste query

---

### 📌 Rest Assured Example

```java
given()
    .baseUri("https://api.spacex.land/graphql/")
    .contentType("application/json")
    .body("{\"query\":\"{ launchesPast(limit:2){ mission_name } }\"}")
.when()
    .post()
.then()
    .statusCode(200);
```

---

# 🧠 My Recommendation (for you)

Since you're doing **automation + API testing**, start with:

1. **GraphQL Zero** → CRUD + mutations
2. **SpaceX API** → real-world complexity
3. **Countries API** → quick practice

---

If you want, I can give you:
✅ Ready-made **Postman collections**
✅ **Rest Assured interview questions (GraphQL)**
✅ **End-to-end test scenarios (real projects)**

[1]: https://graphqlzero.almansi.me/?utm_source=chatgpt.com "GraphQLZero: A Simple, Zero-Config Fake GraphQL API."
[2]: https://openpublicapis.com/api/geographql?utm_source=chatgpt.com "GeographQL - Free Public APIs"
[3]: https://www.postman.com/devrel/graphql-examples/documentation/94xo3pv/free-graphql-apis?utm_source=chatgpt.com "Free GraphQL APIs | Documentation | Postman API Network"
[4]: https://www.postman.com/devrel/graphql-examples?utm_source=chatgpt.com "GraphQL examples | Postman API Network"
