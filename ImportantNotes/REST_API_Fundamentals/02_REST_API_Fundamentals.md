# 🌐 REST API Fundamentals – Complete Guide

## HTTP Methods – Detailed Explanation, Design Considerations & Spring Boot Examples

---

## 📌 What is an HTTP Method?

An **HTTP method** (also called an HTTP verb) defines **what action a client wants to perform on a resource** exposed by a REST API.

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

➡️ *Retrieve the user with ID 101*

---

## 🔑 Why HTTP Methods Are Critical in REST API Design?

* Define **clear intent** (read vs write)
* Enable **standard, predictable APIs**
* Improve **security, caching, and performance**
* Allow **scalability and maintainability**
* Help API tools (Postman, browsers, gateways) behave correctly

---

# 1️⃣ GET – Retrieve Resource (READ)

## 🔹 Function

* Retrieves data from the server
* **Does not modify server state**

---

## 🔹 How GET Works (Flowchart)

```text
Client
  │
  ▼
GET /api/users/101
  │
  ▼
Controller → Service → Database
  │
  ▼
Response (200 OK + JSON)
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

❌ **No change** (GET is read-only)

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

✔ Fast and efficient
✔ Safe for retries
✔ Cache-friendly

## 🔹 Limitations

❌ Sensitive data exposed in URL
❌ URL length limitation
❌ Cannot modify data

---

## 🔹 When to Use

* Fetch records
* Search operations
* Read-only APIs

---

# 2️⃣ POST – Create Resource (CREATE)

## 🔹 Function

* Creates a **new resource**
* Sends data in request body

---

## 🔹 How POST Works

```text
Client
  │
  ▼
POST /api/users
(Request Body JSON)
  │
  ▼
Controller → Service → DB Insert
  │
  ▼
Response (201 Created)
```

---

## 🔹 Postman Example

```http
POST /api/users
```

```json
{
  "name": "Rahul",
  "email": "rahul@gmail.com"
}
```

### Data Before

```json
[]
```

### Data After

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

# 3️⃣ PUT – Full Resource Update

## 🔹 Function

* Replaces the **entire resource**
* Client must send **all fields**

---

## 🔹 How PUT Works

```text
Client
  │
  ▼
PUT /api/users/101
(Full Object)
  │
  ▼
Controller → Service → DB Replace
  │
  ▼
Response (200 OK)
```

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
public User update(@PathVariable int id,
                   @RequestBody User user) {
    return userService.update(id, user);
}
```

---

## 🔹 Advantages

✔ Idempotent
✔ Predictable behavior
✔ Clear replacement semantics

## 🔹 Limitations

❌ Large payloads
❌ Risk of overwriting unchanged fields

---

# 4️⃣ PATCH – Partial Update (Recommended)

## 🔹 Function

* Updates **only specific fields**

---

## 🔹 How PATCH Works

```text
Client
  │
  ▼
PATCH /api/users/101
(Partial JSON)
  │
  ▼
Controller → Merge Logic → DB Update
  │
  ▼
Response (200 OK)
```

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
✔ No duplicate creation
✔ Better performance

## 🔹 Limitations of PATCH

❌ Complex validation logic
❌ Harder to audit changes
❌ Not always idempotent

---

# 5️⃣ DELETE – Remove Resource

## 🔹 Function

* Deletes a resource permanently

---

## 🔹 Flow

```text
Client → DELETE /api/users/101
        → Controller → Service → DB Delete
        ← 204 No Content
```

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

# 6️⃣ HEAD – Metadata Retrieval (Advanced)

## 🔹 What is HEAD?

`HEAD` works **exactly like GET**, but the server **returns only headers**, not the response body.

---

## 🔹 How HEAD Works

```text
Client → HEAD /api/users/101
        → Controller → Service → DB Check
        ← 200 OK (Headers only)
```

---

## 🔹 Why HEAD is Important?

### ✔ Real-World Use Cases

* Check **resource existence**
* Validate **ETag / Last-Modified**
* Perform **lightweight health checks**
* Optimize **network performance**

---

## 🔹 HEAD vs GET

| Feature       | HEAD   | GET    |
| ------------- | ------ | ------ |
| Response Body | ❌      | ✅      |
| Headers       | ✅      | ✅      |
| Performance   | Faster | Slower |

---

## 🔹 Spring Boot Support

```java
@RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
public ResponseEntity<Void> headUser(@PathVariable int id) {
    return userService.exists(id)
            ? ResponseEntity.ok().build()
            : ResponseEntity.notFound().build();
}
```

---

## 🔹 Limitations

❌ Rarely used by developers
❌ Often overlooked in API design

---

# 7️⃣ OPTIONS – Capabilities & CORS

## 🔹 What is OPTIONS?

`OPTIONS` tells the client **what HTTP methods are supported** for a given resource.

---

## 🔹 OPTIONS Flow (CORS Preflight)

```text
Browser
  │
  ▼
OPTIONS /api/users
(Preflight)
  │
  ▼
Server responds:
Allow: GET, POST, PUT, DELETE
```

---

## 🔹 Why OPTIONS is Critical?

✔ Mandatory for **CORS preflight**
✔ Enforced by browsers
✔ Prevents illegal cross-origin calls

---

## 🔹 Example Response

```http
Allow: GET, POST, PUT, DELETE
Access-Control-Allow-Origin: *
```

---

## 🔹 Spring Boot Handling

```java
@CrossOrigin(origins = "*")
@RestController
public class UserController {
}
```

---

## 🔹 Limitations

❌ Mostly browser-driven
❌ Rarely tested manually

---

# 8️⃣ TRACE – Debugging (Security Sensitive)

## 🔹 What is TRACE?

`TRACE` echoes the **exact request back to the client**.

---

## 🔹 How TRACE Works

```text
Client → TRACE /api/users
        ← Request echoed back
```

---

## 🔹 Why TRACE Exists?

✔ Diagnostic and debugging
✔ Proxy testing

---

## 🔹 Why TRACE is Disabled?

❌ Vulnerable to XST attacks
❌ Exposes headers & tokens

➡️ **Always disable TRACE in production**

---

# 9️⃣ CONNECT – Tunneling (Awareness)

* Used to establish HTTPS tunnels via proxy
* ❌ Not used in REST APIs directly

---

# 📊 HTTP Methods Summary Table

| Method  | Safe | Idempotent | Cacheable | Use Case       |
| ------- | ---- | ---------- | --------- | -------------- |
| GET     | ✅    | ✅          | ✅         | Fetch          |
| POST    | ❌    | ❌          | ❌         | Create         |
| PUT     | ❌    | ✅          | ❌         | Replace        |
| PATCH   | ❌    | ⚠️         | ❌         | Partial update |
| DELETE  | ❌    | ✅          | ❌         | Remove         |
| HEAD    | ✅    | ✅          | ✅         | Metadata       |
| OPTIONS | ✅    | ✅          | ❌         | CORS           |
| TRACE   | ❌    | ❌          | ❌         | Debug          |

---

# 🧩 Spring Boot REST Design Best Practices

✔ Correct HTTP method usage
✔ Stateless APIs
✔ Meaningful status codes
✔ PATCH for partial updates
✔ Disable TRACE in production

---

# 📚 Official Reference Documentation

* **HTTP Semantics (RFC 9110)**
  [https://www.rfc-editor.org/rfc/rfc9110](https://www.rfc-editor.org/rfc/rfc9110)

* **REST API Design – Microsoft**
  [https://learn.microsoft.com/en-us/azure/architecture/best-practices/api-design](https://learn.microsoft.com/en-us/azure/architecture/best-practices/api-design)

* **Spring Web MVC Documentation**
  [https://docs.spring.io/spring-framework/reference/web/webmvc.html](https://docs.spring.io/spring-framework/reference/web/webmvc.html)

---

## ✅ Final Conclusion

Correct usage of HTTP methods leads to:

✔ Clean REST architecture
✔ Secure APIs
✔ Predictable behavior
✔ Enterprise-grade Spring Boot applications

---