
# 🌐 REST API Fundamentals

## HTTP Methods – Function, Importance & Examples (Spring Boot)

---

## 📌 What is an HTTP Method?

HTTP methods (also called **HTTP verbs**) define **what action a client wants to perform on a resource** exposed by a REST API.

In RESTful architecture:

* **Resources** are identified by URLs
* **Actions** are defined by HTTP methods

Example:

```http
GET /api/users/101
```

➡️ “Get user with ID 101”

---

## 🔑 Why HTTP Methods Are Important?

* They define **clear intent** (read, create, update, delete)
* They enable **standardization** across APIs
* They help with **security, caching, performance, and scalability**
* REST APIs rely on correct HTTP method usage

---

# 1️⃣ GET – Retrieve Data (READ)

![Image](https://kajabi-storefronts-production.kajabi-cdn.com/kajabi-storefronts-production/file-uploads/sites/146797/images/3c4fb-1b0-880c-b1aa-83f81cb80acf_rest-api-model-http-request-response.webp?utm_source=chatgpt.com)

![Image](https://s3.us-west-1.wasabisys.com/idbwmedia.com/images/api/restapi_restapi.svg?utm_source=chatgpt.com)

### 🔹 Function

* Fetches data from the server
* Does **NOT modify** server state

### 🔹 Key Characteristics

* Safe
* Idempotent
* Cacheable

### 🔹 Example HTTP Request

```http
GET /api/users/101
```

### 🔹 Spring Boot Example

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }
}
```

### 🔹 When to Use

* Fetch single record
* Fetch list of records
* Search / filter data

---

# 2️⃣ POST – Create New Resource

![Image](https://media.licdn.com/dms/image/v2/D4D12AQHxG4Prn4ZrBQ/article-cover_image-shrink_600_2000/article-cover_image-shrink_600_2000/0/1718998421506?e=2147483647\&t=-2LQBR1-lDFdKqTy66DNfGm4cTz7xy27k8-QYYsYaio\&v=beta\&utm_source=chatgpt.com)

![Image](https://community.retool.com/uploads/default/original/3X/b/b/bbd4b149c9928487e22304c8d0c40b36a0aa6eb9.png?utm_source=chatgpt.com)

### 🔹 Function

* Sends data to the server
* Creates a **new resource**

### 🔹 Key Characteristics

* Not idempotent
* Request body required
* Cannot be cached

### 🔹 Example HTTP Request

```http
POST /api/users
Content-Type: application/json

{
  "name": "Rahul",
  "email": "rahul@gmail.com"
}
```

### 🔹 Spring Boot Example

```java
@PostMapping
public User createUser(@RequestBody User user) {
    return userService.saveUser(user);
}
```

### 🔹 When to Use

* Registration
* Form submission
* Insert operations

---

# 3️⃣ PUT – Update Entire Resource

![Image](https://media.licdn.com/dms/image/v2/D5622AQGcWdCsUyG4Gg/feedshare-shrink_2048_1536/B56ZUt6N3BGUAo-/0/1740231993942?e=2147483647\&t=4SyGWljb3DsfOUqOEPgvGqJ-Y0a9eciJVhboY7futus\&v=beta\&utm_source=chatgpt.com)

![Image](https://kodekloud.com/blog/content/images/2023/04/data-src-image-edb98e03-4aaf-4b27-85c3-cf6e0f4f2a7a.png?utm_source=chatgpt.com)

### 🔹 Function

* Replaces the **entire resource**
* All fields must be sent

### 🔹 Key Characteristics

* Idempotent
* Full update

### 🔹 Example HTTP Request

```http
PUT /api/users/101
{
  "name": "Rahul Sharma",
  "email": "rahul.sharma@gmail.com"
}
```

### 🔹 Spring Boot Example

```java
@PutMapping("/{id}")
public User updateUser(
        @PathVariable int id,
        @RequestBody User user) {
    return userService.updateUser(id, user);
}
```

### 🔹 When to Use

* Complete object replacement
* Admin-level updates

---

# 4️⃣ PATCH – Partial Update

![Image](https://i.sstatic.net/K7NRB.png?utm_source=chatgpt.com)

![Image](https://cdn.prod.website-files.com/610d78d90f895fbe6aef8810/64076186aa8de199d0516717_image002.png?utm_source=chatgpt.com)

### 🔹 Function

* Updates **specific fields only**

### 🔹 Key Characteristics

* Partial update
* Efficient
* Not always idempotent

### 🔹 Example HTTP Request

```http
PATCH /api/users/101
{
  "email": "newemail@gmail.com"
}
```

### 🔹 Spring Boot Example

```java
@PatchMapping("/{id}")
public User updateUserPartially(
        @PathVariable int id,
        @RequestBody Map<String, Object> updates) {
    return userService.partialUpdate(id, updates);
}
```

### 🔹 When to Use

* Update one or two fields
* Performance-sensitive APIs

---

# 5️⃣ DELETE – Remove Resource

![Image](https://media.licdn.com/dms/image/v2/D4D12AQHxG4Prn4ZrBQ/article-cover_image-shrink_600_2000/article-cover_image-shrink_600_2000/0/1718998421506?e=2147483647\&t=-2LQBR1-lDFdKqTy66DNfGm4cTz7xy27k8-QYYsYaio\&v=beta\&utm_source=chatgpt.com)

![Image](https://bezkoder.com/wp-content/uploads/2020/04/django-rest-api-tutorial-example-delete-method-one.png?utm_source=chatgpt.com)

### 🔹 Function

* Deletes resource from server

### 🔹 Key Characteristics

* Idempotent
* No request body

### 🔹 Example HTTP Request

```http
DELETE /api/users/101
```

### 🔹 Spring Boot Example

```java
@DeleteMapping("/{id}")
public void deleteUser(@PathVariable int id) {
    userService.deleteUser(id);
}
```

### 🔹 When to Use

* Remove records
* Cleanup operations

---

# 6️⃣ HEAD – Metadata Only

![Image](https://docs.trafficserver.apache.org/en/latest/_images/http_header_struct.jpg?utm_source=chatgpt.com)

![Image](https://i0.wp.com/automatenow.io/wp-content/uploads/2023/10/http-request-methods.png?resize=744%2C1024\&ssl=1\&utm_source=chatgpt.com)

### 🔹 Function

* Same as GET but **no response body**

### 🔹 Use Cases

* Check resource existence
* Cache validation
* Health checks

```http
HEAD /api/users/101
```

---

# 7️⃣ OPTIONS – Supported Operations (CORS)

![Image](https://i.sstatic.net/6jsKY.png?utm_source=chatgpt.com)

![Image](https://howtodoinjava.com/wp-content/uploads/2019/05/Spring-MVC-Options-request-handler.png?utm_source=chatgpt.com)

### 🔹 Function

* Returns allowed HTTP methods

### 🔹 Importance

* Required for **CORS preflight**
* Browser security mechanism

```http
OPTIONS /api/users
```

---

# 8️⃣ TRACE – Debugging

### 🔹 Function

* Echoes request back to client

### 🔹 Status

* ❌ Disabled in most production systems (security risk)

---

# 9️⃣ CONNECT – Tunneling

### 🔹 Function

* Establishes a tunnel (HTTPS via proxy)

### 🔹 Usage

* Browser / proxy communication
* Not used in REST APIs directly

---

# 📊 HTTP Methods Summary Table

| Method  | Purpose        | Request Body | Idempotent | Common Usage     |
| ------- | -------------- | ------------ | ---------- | ---------------- |
| GET     | Read data      | ❌            | ✅          | Fetch records    |
| POST    | Create         | ✅            | ❌          | Insert data      |
| PUT     | Full update    | ✅            | ✅          | Replace resource |
| PATCH   | Partial update | ✅            | ⚠️         | Modify fields    |
| DELETE  | Remove         | ❌            | ✅          | Delete record    |
| HEAD    | Metadata       | ❌            | ✅          | Validation       |
| OPTIONS | Capabilities   | ❌            | ✅          | CORS             |
| TRACE   | Debug          | ❌            | ❌          | Testing          |
| CONNECT | Tunnel         | ❌            | ❌          | HTTPS proxy      |

---

# 🔁 REST API CRUD Mapping

```text
GET    /users        → Read all users
GET    /users/{id}   → Read single user
POST   /users        → Create user
PUT    /users/{id}   → Update user
PATCH  /users/{id}   → Partial update
DELETE /users/{id}   → Delete user
```

---

# 🎯 Best Practices

✔ Use correct HTTP methods
✔ Follow REST naming conventions
✔ Return proper HTTP status codes
✔ Avoid misuse of GET for updates
✔ Use PATCH for partial updates

---

## ✅ Conclusion

Understanding HTTP methods is **fundamental to REST API design**.
Correct usage ensures:

* Clean architecture
* Better security
* Scalability
* Maintainability

---