# 🌐 REST API Fundamentals – Complete Guide

## HTTP Methods – Detailed Explanation, Design Considerations & Spring Boot Examples

---

## 📌 What is an HTTP Method?

An **HTTP method** (also known as an HTTP verb) defines **the action a client wants to perform on a resource** in a REST API.

In REST architecture:

| Concept     | Description                            |
| ----------- | -------------------------------------- |
| Resource    | Any data object (User, Order, Product) |
| URL         | Identifies the resource                |
| HTTP Method | Defines the action                     |

### Example

```http
GET /api/users/101
```

➡️ Fetch user with ID `101`

---

## 🔑 Why HTTP Methods Are Critical in REST API Design?

* Define **intent clearly**
* Enable **standard, predictable APIs**
* Improve **security & caching**
* Allow **scalability & maintainability**
* Help tools like **Postman, browsers, gateways** behave correctly

---

# 1️⃣ GET – Retrieve Resource (READ)

![Image](https://www.researchgate.net/publication/369358390/figure/fig1/AS%3A11431281127810255%401679180216268/HTTP-request-and-response-flow.png?utm_source=chatgpt.com)

![Image](https://kajabi-storefronts-production.kajabi-cdn.com/kajabi-storefronts-production/file-uploads/blogs/2147485434/images/315487-7af3-d17-c8af-0b4d8af188db_rest-api-model-diagram.png?utm_source=chatgpt.com)

## 🔹 Function

* Retrieves data from server
* Does **not modify server state**

---

## 🔹 How GET Works (Flow)

```text
Client → GET Request → Controller → Service → Database
                               ← Response (JSON)
```

---

## 🔹 Postman Example

### Request

```http
GET /api/users/101
```

### Data Before Request

```json
{
  "id": 101,
  "name": "Rahul",
  "email": "rahul@gmail.com"
}
```

### Data After Request

❌ **No change** (Read-only)

---

## 🔹 Spring Boot Example

```java
@GetMapping("/{id}")
public User getUser(@PathVariable int id) {
    return userService.findById(id);
}
```

---

## 🔹 Features (Design Perspective)

| Feature      | Supported |
| ------------ | --------- |
| Safe         | ✅         |
| Idempotent   | ✅         |
| Cacheable    | ✅         |
| Request Body | ❌         |

---

## 🔹 Advantages

✔ Fast
✔ Cache-friendly
✔ Safe for repeated calls

## 🔹 Limitations

❌ Cannot send sensitive data in URL
❌ URL length limitation
❌ Not for updates

---

## 🔹 When to Use

* Fetch records
* Search operations
* Read-only endpoints

---

# 2️⃣ POST – Create Resource (CREATE)

![Image](https://svg.template.creately.com/iqrvjwja1?utm_source=chatgpt.com)

![Image](https://kajabi-storefronts-production.kajabi-cdn.com/kajabi-storefronts-production/file-uploads/blogs/2147485434/images/315487-7af3-d17-c8af-0b4d8af188db_rest-api-model-diagram.png?utm_source=chatgpt.com)

## 🔹 Function

* Creates a **new resource**
* Sends data in request body

---

## 🔹 How POST Works

```text
Client → POST Request (JSON)
        → Controller → Service → DB Insert
        ← Response (201 Created)
```

---

## 🔹 Postman Example

### Request

```http
POST /api/users
```

```json
{
  "name": "Rahul",
  "email": "rahul@gmail.com"
}
```

### Data Before Request

```json
[]
```

### Data After Request

```json
{
  "id": 102,
  "name": "Rahul",
  "email": "rahul@gmail.com"
}
```

---

## 🔹 Spring Boot Example

```java
@PostMapping
public User createUser(@RequestBody User user) {
    return userService.save(user);
}
```

---

## 🔹 Features

| Feature           | Value |
| ----------------- | ----- |
| Idempotent        | ❌     |
| Cacheable         | ❌     |
| Request Body      | ✅     |
| Resource Creation | ✅     |

---

## 🔹 Advantages

✔ Flexible payload
✔ Supports complex objects
✔ Ideal for inserts

## 🔹 Limitations

❌ Duplicate records if retried
❌ Not cacheable

---

## 🔹 POST vs PUT vs PATCH

| Feature          | POST | PUT | PATCH |
| ---------------- | ---- | --- | ----- |
| Creates resource | ✅    | ❌   | ❌     |
| Full update      | ❌    | ✅   | ❌     |
| Partial update   | ❌    | ❌   | ✅     |

---

# 3️⃣ PUT – Full Resource Update

![Image](https://media.licdn.com/dms/image/v2/D4D12AQHxG4Prn4ZrBQ/article-cover_image-shrink_600_2000/article-cover_image-shrink_600_2000/0/1718998421506?e=2147483647\&t=-2LQBR1-lDFdKqTy66DNfGm4cTz7xy27k8-QYYsYaio\&v=beta\&utm_source=chatgpt.com)

![Image](https://javacodehouse.com/assets/img/thumb/PUT-vs-PATCH.svg?utm_source=chatgpt.com)

## 🔹 Function

* Replaces **entire resource**
* Client must send all fields

---

## 🔹 Postman Example

```http
PUT /api/users/101
```

```json
{
  "name": "Rahul Sharma",
  "email": "rahul.sharma@gmail.com"
}
```

### Before

```json
{
  "name": "Rahul",
  "email": "rahul@gmail.com"
}
```

### After

```json
{
  "name": "Rahul Sharma",
  "email": "rahul.sharma@gmail.com"
}
```

---

## 🔹 Spring Boot Example

```java
@PutMapping("/{id}")
public User update(@PathVariable int id, @RequestBody User user) {
    return userService.update(id, user);
}
```

---

## 🔹 Advantages

✔ Predictable
✔ Idempotent
✔ Clean replacement

## 🔹 Limitations

❌ Payload heavy
❌ Risk of overwriting fields

---

# 4️⃣ PATCH – Partial Update (Recommended)

![Image](https://learn.microsoft.com/en-us/azure/cosmos-db/media/partial-document-update/patch-multi-region-conflict-resolution.png?utm_source=chatgpt.com)

![Image](https://i.sstatic.net/K7NRB.png?utm_source=chatgpt.com)

## 🔹 Function

* Updates **specific fields only**

---

## 🔹 Postman Example

```http
PATCH /api/users/101
```

```json
{
  "email": "new@gmail.com"
}
```

### Before

```json
{
  "name": "Rahul",
  "email": "rahul@gmail.com"
}
```

### After

```json
{
  "name": "Rahul",
  "email": "new@gmail.com"
}
```

---

## 🔹 Spring Boot Example

```java
@PatchMapping("/{id}")
public User patchUpdate(@PathVariable int id,
                        @RequestBody Map<String, Object> updates) {
    return userService.partialUpdate(id, updates);
}
```

---

## 🔹 Advantages Over POST

✔ Smaller payload
✔ Performance efficient
✔ No duplicate creation

## 🔹 Limitations

❌ Complex validation
❌ Harder to audit
❌ Not always idempotent

---

# 5️⃣ DELETE – Remove Resource

![Image](https://www.oreilly.com/api/v2/epubs/urn%3Aorm%3Abook%3A9781788294041/files/assets/dd386c3a-40f7-4a4e-93f8-1a663670446e.png?utm_source=chatgpt.com)

![Image](https://browserstack.wpenginepowered.com/wp-content/uploads/2025/08/DELETE-Method-in-HTTP.png?utm_source=chatgpt.com)

## 🔹 Function

* Deletes resource

---

## 🔹 Postman Example

```http
DELETE /api/users/101
```

### Before

```json
{ "id": 101 }
```

### After

```json
❌ Resource removed
```

---

## 🔹 Features

| Feature      | Supported |
| ------------ | --------- |
| Idempotent   | ✅         |
| Request Body | ❌         |

---

# 6️⃣ HEAD – Metadata Only

![Image](https://cdn.prod.website-files.com/610d78d90f895fbe6aef8810/646bed02a9a8ab19c58958ed_608f00f93290a12018d8d024_header1.png?utm_source=chatgpt.com)

![Image](https://cdn.tutsplus.com/cdn-cgi/image/width%3D537/net/uploads/legacy/511_http/http_diagram.png?utm_source=chatgpt.com)

* Same as GET but **no body**
* Used for cache checks

---

# 7️⃣ OPTIONS – CORS & Capabilities

![Image](https://webperf.tips/static/9ffa09d6939aa3f1193f17e05ecd3a3e/906b5/OptimizingCORS01.png?utm_source=chatgpt.com)

![Image](https://developer.chrome.com/static/blog/private-network-access-preflight/image/sequence-diagram-represe-efb5dbdcde5d7.jpg?utm_source=chatgpt.com)

* Used for **CORS preflight**
* Returns allowed methods

---

# 8️⃣ TRACE & CONNECT (Awareness)

| Method  | Purpose | Usage      |
| ------- | ------- | ---------- |
| TRACE   | Debug   | ❌ Disabled |
| CONNECT | Tunnel  | Proxy only |

---

# 📊 HTTP Methods Summary Table

| Method | Safe | Idempotent | Cacheable | Use Case       |
| ------ | ---- | ---------- | --------- | -------------- |
| GET    | ✅    | ✅          | ✅         | Fetch          |
| POST   | ❌    | ❌          | ❌         | Create         |
| PUT    | ❌    | ✅          | ❌         | Replace        |
| PATCH  | ❌    | ⚠️         | ❌         | Partial update |
| DELETE | ❌    | ✅          | ❌         | Remove         |

---

# 🧩 Spring Boot REST Design Best Practices

✔ Correct HTTP method usage
✔ Stateless APIs
✔ Meaningful status codes
✔ Use PATCH for updates
✔ Avoid POST misuse

---

# 📚 Official Reference Documentation

🔗 HTTP Methods (RFC 9110):
[https://www.rfc-editor.org/rfc/rfc9110](https://www.rfc-editor.org/rfc/rfc9110)

🔗 REST API Design Guide (Microsoft):
[https://learn.microsoft.com/en-us/azure/architecture/best-practices/api-design](https://learn.microsoft.com/en-us/azure/architecture/best-practices/api-design)

🔗 Spring Boot REST Docs:
[https://docs.spring.io/spring-framework/reference/web/webmvc.html](https://docs.spring.io/spring-framework/reference/web/webmvc.html)

---

## ✅ Final Conclusion

Mastering HTTP methods ensures:

✔ Clean REST design
✔ Predictable APIs
✔ Better performance
✔ Industry-standard practices

---