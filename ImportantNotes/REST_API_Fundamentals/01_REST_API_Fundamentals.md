# 🌐 REST API Fundamentals

## HTTP Methods, Status Codes & Practical Examples (Spring Boot + Postman)

---

## 📌 What is a REST API?

A **REST API (Representational State Transfer API)** allows clients (browser, mobile app, Postman) to communicate with servers using **HTTP protocol**.

REST APIs are built around:

* **Resources** (Users, Orders, Products)
* **HTTP Methods** (GET, POST, PUT, etc.)
* **HTTP Status Codes**
* **JSON/XML payloads**

---

## 🔑 Why HTTP Methods & Status Codes Matter?

* They define **clear communication rules**
* They improve **readability, consistency, and debugging**
* They are mandatory for **RESTful API design**
* Used by browsers, mobile apps, microservices

---

# 🔹 HTTP METHODS (Quick Recap)

| Method  | Purpose         |
| ------- | --------------- |
| GET     | Read data       |
| POST    | Create data     |
| PUT     | Full update     |
| PATCH   | Partial update  |
| DELETE  | Remove data     |
| HEAD    | Metadata        |
| OPTIONS | Allowed methods |
| TRACE   | Debug           |
| CONNECT | Tunnel          |

---

# 📘 HTTP STATUS CODES (NEW SECTION)

![Image](https://assets.bytebytego.com/diagrams/0233-http-status-code.png?utm_source=chatgpt.com)

![Image](https://restfulapi.net/wp-content/uploads/HTTP-Error-Codes.jpg?utm_source=chatgpt.com)

## 📌 What Are HTTP Status Codes?

HTTP status codes are **server responses** that indicate the **result of a client request**.

They are grouped by **number ranges**:

| Range | Category      | Meaning               |
| ----- | ------------- | --------------------- |
| 1xx   | Informational | Request received      |
| 2xx   | Success       | Request successful    |
| 3xx   | Redirection   | Further action needed |
| 4xx   | Client Error  | Client mistake        |
| 5xx   | Server Error  | Server failure        |

---

## ✅ 2xx – Success Codes

| Code | Name       | Meaning            | When Used        |
| ---- | ---------- | ------------------ | ---------------- |
| 200  | OK         | Request successful | GET, PUT         |
| 201  | Created    | Resource created   | POST             |
| 202  | Accepted   | Request accepted   | Async processing |
| 204  | No Content | Success, no body   | DELETE           |

### Spring Boot Example

```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@RequestBody User user) {
    User saved = userService.save(user);
    return new ResponseEntity<>(saved, HttpStatus.CREATED);
}
```

---

## ❌ 4xx – Client Error Codes

| Code | Name         | Meaning            |
| ---- | ------------ | ------------------ |
| 400  | Bad Request  | Invalid input      |
| 401  | Unauthorized | Not logged in      |
| 403  | Forbidden    | Access denied      |
| 404  | Not Found    | Resource not found |
| 409  | Conflict     | Duplicate data     |

### Example

```java
@GetMapping("/users/{id}")
public ResponseEntity<User> getUser(@PathVariable int id) {
    return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

---

## 🔥 5xx – Server Error Codes

| Code | Name                  | Meaning           |
| ---- | --------------------- | ----------------- |
| 500  | Internal Server Error | Application crash |
| 502  | Bad Gateway           | Invalid upstream  |
| 503  | Service Unavailable   | Server down       |

✔ These indicate **backend problems**, not client issues.

---

# 🧪 POSTMAN EXAMPLES (NEW)

## 🔹 GET Request (Postman)

* Method: **GET**
* URL:

```
http://localhost:8080/api/users/1
```

✔ Response:

```json
{
  "id": 1,
  "name": "Rahul",
  "email": "rahul@gmail.com"
}
```

---

## 🔹 POST Request (Postman)

* Method: **POST**
* URL:

```
http://localhost:8080/api/users
```

* Headers:

```
Content-Type: application/json
```

* Body (raw → JSON):

```json
{
  "name": "Amit",
  "email": "amit@gmail.com"
}
```

✔ Response Code: **201 Created**

---

## 🔹 PUT Request (Postman)

```
PUT http://localhost:8080/api/users/1
```

```json
{
  "name": "Amit Sharma",
  "email": "amit.sharma@gmail.com"
}
```

---

## 🔹 PATCH Request (Postman)

```
PATCH http://localhost:8080/api/users/1
```

```json
{
  "email": "updated@gmail.com"
}
```

---

## 🔹 DELETE Request (Postman)

```
DELETE http://localhost:8080/api/users/1
```

✔ Response Code: **204 No Content**

---

# 💻 cURL EXAMPLES (NEW)

## 🔹 GET

```bash
curl -X GET http://localhost:8080/api/users/1
```

## 🔹 POST

```bash
curl -X POST http://localhost:8080/api/users \
-H "Content-Type: application/json" \
-d '{"name":"Rahul","email":"rahul@gmail.com"}'
```

## 🔹 PUT

```bash
curl -X PUT http://localhost:8080/api/users/1 \
-H "Content-Type: application/json" \
-d '{"name":"Rahul Sharma","email":"rahul.sharma@gmail.com"}'
```

## 🔹 PATCH

```bash
curl -X PATCH http://localhost:8080/api/users/1 \
-H "Content-Type: application/json" \
-d '{"email":"new@gmail.com"}'
```

## 🔹 DELETE

```bash
curl -X DELETE http://localhost:8080/api/users/1
```

---

# 🔁 REST API CRUD MAPPING

```text
GET     /users        → Read all users
GET     /users/{id}   → Read user
POST    /users        → Create user
PUT     /users/{id}   → Update user
PATCH   /users/{id}   → Partial update
DELETE  /users/{id}   → Delete user
```

---

# 🧠 BEST PRACTICES

✔ Always return correct HTTP status codes
✔ Use POST for creation, not GET
✔ Use PATCH for partial updates
✔ Do not expose server errors to client
✔ Follow REST naming conventions

---

# ✅ CONCLUSION

HTTP methods + HTTP status codes form the **foundation of REST APIs**.
Mastering them ensures:

* Clean API design
* Better client-server communication
* Easier debugging
* Production-ready services

---