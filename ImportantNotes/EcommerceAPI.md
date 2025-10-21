# E-Commerce API — Contract & Runbook (real services)

This document describes the **real** E-commerce APIs (not mocks): request/response contracts, headers, examples and a **step-by-step run sequence** so the full flow executes correctly.

---

## Prerequisites / shared conventions

* **Base URL:** `https://rahulshettyacademy.com/api/ecom`
  (use `{{baseUrl}}` in Postman / scripts)
* **Authentication:** JWT token returned by **EcomLogin**. Include exactly the token string in the `Authorization` header for protected endpoints:

  ```
  Authorization: <token>
  ```

  > Note: the service expects the raw token value (no `Bearer ` prefix) — use the token exactly as returned.
* **Content-Types:**

    * JSON requests: `Content-Type: application/json`
    * File upload (AddProduct): `Content-Type: multipart/form-data` (boundary set by client)
* All request/response examples show typical values. Field names, casing and types must be used exactly as shown.

---

# 1️⃣ EcomLogin

**Purpose:** Authenticate user and return a JWT token + userId.

**Endpoint**

* `POST https://rahulshettyacademy.com/api/ecom/auth/login`

**Request**

* Headers:

  ```
  Content-Type: application/json
  ```
* Body (JSON):

  ```json
  {
    "userEmail": "ratnakarkolhatkar@gmail.com",
    "userPassword": "Ratanlord@1409"
  }
  ```

    * `userEmail` (string, required): registered email
    * `userPassword` (string, required): password

**Successful Response**

* Status: `200 OK`
* Body:

  ```json
  {
    "token": "<JWT_TOKEN>",
    "userId": "6344e9b8c4d0c51f4f3aaf48",
    "message": "Login Successfully"
  }
  ```

    * `token` (string): JWT to use in `Authorization` header in subsequent calls
    * `userId` (string): identifier to be used as `productAddedBy` when adding products

**Errors**

* `401 Unauthorized` — invalid credentials
* `400 Bad Request` — missing fields

**Curl example**

```bash
curl -s -X POST "{{baseUrl}}/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"userEmail":"ratnakarkolhatkar@gmail.com","userPassword":"Ratanlord@1409"}'
```

---

# 2️⃣ AddProduct

**Purpose:** Add a new product (multipart/form-data, includes file upload). Requires authentication.

**Endpoint**

* `POST https://rahulshettyacademy.com/api/ecom/product/add-product`

**Headers**

* `Authorization: <token>`
* `Content-Type: multipart/form-data` (set automatically by client/form library)

**Form-Data (multipart) fields**

* `productName` (string, required)
* `productAddedBy` (string, required) — **use `userId`** returned from **EcomLogin**
* `productCategory` (string, required)
* `productSubCategory` (string, optional)
* `productPrice` (number/string, required)
* `productDescription` (string, optional)
* `productFor` (string, optional) — e.g., `woman`, `man`, `kid`
* `productImage` (file, required) — local image file

**Successful Response**

* Status: `201 Created`
* Body:

  ```json
  {
    "productId": "68f748fdf669d6cb0a214f4d",
    "message": "Product Added Successfully"
  }
  ```

    * `productId` (string): unique product identifier — **save this** for order creation and for deletion.

**Errors**

* `401 Unauthorized` — missing/invalid token
* `400 Bad Request` — missing required form fields or invalid file
* `415 Unsupported Media Type` — if file or content type is invalid

**Notes**

* The `Authorization` header must contain the token exactly as returned by Login.
* The request must be `multipart/form-data`. Ensure your client sets a boundary and includes the file part named `productImage`.
* Many GUI clients (Postman) require selecting the key type `file` for `productImage`.

**Curl (example using `curl -F`)**

```bash
curl -s -X POST "{{baseUrl}}/product/add-product" \
  -H "Authorization: {{token}}" \
  -F "productName=qwerty" \
  -F "productAddedBy=6344e9b8c4d0c51f4f3aaf48" \
  -F "productCategory=fashion" \
  -F "productSubCategory=shirts" \
  -F "productPrice=11500" \
  -F "productDescription=Addidas Originals" \
  -F "productFor=woman" \
  -F "productImage=@/path/to/local/image.jpg"
```

---

# 3️⃣ CreateOrder

**Purpose:** Create an order for one or more products. Requires authentication.

**Endpoint**

* `POST https://rahulshettyacademy.com/api/ecom/order/create-order`

**Headers**

* `Authorization: <token>`
* `Content-Type: application/json`

**Request Body (JSON)**

```json
{
  "orders": [
    {
      "country": "India",
      "productOrderedId": "68f748fdf669d6cb0a214f4d"
    }
  ]
}
```

* `orders` (array, required)

    * each element:

        * `country` (string, required)
        * `productOrderedId` (string, required) — **use `productId`** from AddProduct response

**Successful Response**

* Status: `201 Created`
* Body:

```json
{
  "orders": ["68f74930f669d6cb0a214f8d"],
  "productOrderId": ["68f748fdf669d6cb0a214f4d"],
  "message": "Order Placed Successfully"
}
```

* `orders` (array of orderIds) — **save first element** as `orderId` for viewing order details
* `productOrderId` (array) — product IDs included in the order

**Errors**

* `401 Unauthorized` — missing/invalid token
* `400 Bad Request` — missing/invalid body, product not found

**Curl example**

```bash
curl -s -X POST "{{baseUrl}}/order/create-order" \
  -H "Authorization: {{token}}" \
  -H "Content-Type: application/json" \
  -d '{ "orders":[{"country":"India","productOrderedId":"{{productId}}"}] }'
```

---

# 4️⃣ ViewOrderDetails

**Purpose:** Retrieve full order detail by `orderId`. Requires authentication.

**Endpoint**

* `GET https://rahulshettyacademy.com/api/ecom/order/get-orders-details?id=<orderId>`

**Headers**

* `Authorization: <token>`

**Query Parameter**

* `id` (string, required): the `orderId` returned by CreateOrder

**Successful Response**

* Status: `200 OK`
* Body:

```json
{
  "data": {
    "_id": "68f74930f669d6cb0a214f8d",
    "orderById": "6344e9b8c4d0c51f4f3aaf48",
    "orderBy": "ratnakarkolhatkar@gmail.com",
    "productOrderedId": "68f748fdf669d6cb0a214f4d",
    "productName": "qwerty",
    "country": "India",
    "productDescription": "Addidas Originals",
    "productImage": "https://rahulshettyacademy.com/api/ecom/uploads/productImage_1761036541351.jpg",
    "orderPrice": "11500",
    "__v": 0
  },
  "message": "Orders fetched for customer Successfully"
}
```

* `data._id` equals requested `id`. Verify fields like `productOrderedId` match expected `productId`.

**Errors**

* `401 Unauthorized` — missing/invalid token
* `404 Not Found` — orderId not found
* `400 Bad Request` — missing `id` param

**Curl example**

```bash
curl -s -X GET "{{baseUrl}}/order/get-orders-details?id={{orderId}}" \
  -H "Authorization: {{token}}"
```

---

# 5️⃣ DeleteProduct

**Purpose:** Delete a product by `productId`. Requires authentication.

**Endpoint**

* `DELETE https://rahulshettyacademy.com/api/ecom/product/delete-product/{productId}`

**Headers**

* `Authorization: <token>`

**Path Parameter**

* `{productId}` — the `productId` from AddProduct

**Successful Response**

* Status: `200 OK`
* Body:

```json
{
  "message": "Product Deleted Successfully"
}
```

**Errors**

* `401 Unauthorized` — missing/invalid token
* `404 Not Found` — productId not found
* `403 Forbidden` — user not authorized to delete that product

**Curl example**

```bash
curl -s -X DELETE "{{baseUrl}}/product/delete-product/{{productId}}" \
  -H "Authorization: {{token}}"
```

---

## Step-by-step: How to run the APIs in order (correct execution flow)

Follow these exact steps to run the entire flow end-to-end. Use Postman, curl, or any HTTP client that supports multipart uploads.

1. **Login (EcomLogin)**

    * Call `POST /auth/login` with valid `userEmail` + `userPassword`.
    * Save `token` and `userId`.
    * Example: set Postman collection variables `token` and `userId`.

2. **Add Product (AddProduct)**

    * Call `POST /product/add-product`.

    * Headers: `Authorization: {{token}}`.

    * Body: `multipart/form-data`.

        * Set `productAddedBy` = `{{userId}}`.
        * Attach a local file in `productImage`.

    * Response gives `productId`. Save it as `productId`.

   > Tip: In Postman set `productImage` field type to **file** and select your local image.

3. **Create Order (CreateOrder)**

    * Call `POST /order/create-order`.
    * Headers: `Authorization: {{token}}`, `Content-Type: application/json`.
    * Body: include `productOrderedId` = `{{productId}}`.
    * Response returns `orders` array. Save first element as `orderId`.

4. **View Order Details (ViewOrderDetails)**

    * Call `GET /order/get-orders-details?id={{orderId}}`
    * Headers: `Authorization: {{token}}`
    * Verify `data._id === orderId` and `data.productOrderedId === productId`. Confirm other fields.

5. **Delete Product (DeleteProduct)**

    * Call `DELETE /product/delete-product/{{productId}}`
    * Headers: `Authorization: {{token}}`
    * Confirm `message` is `Product Deleted Successfully`.

6. **(Optional) Validate cleanup**

    * Attempt `GET` or `ViewOrderDetails` again if you expect product still in order history — the product may still appear in order details even if deleted (depends on service behavior). If needed, confirm expected system behavior with backend team.

---

## Example Postman variable setup

* `baseUrl = https://rahulshettyacademy.com/api/ecom`
* `token = ` (filled after login)
* `userId = ` (filled after login)
* `productId = ` (filled after AddProduct)
* `orderId = ` (filled after CreateOrder)

Use test scripts or manual variable assignment to store returned fields into these variables (you previously asked for an automated Postman collection which saves these).

---

## Validation rules & edge cases to test

* Try login with invalid credentials → expect `401`.
* AddProduct without token → expect `401`.
* AddProduct with missing `productImage` or missing `productAddedBy` → expect `400`.
* CreateOrder with invalid `productOrderedId` → expect `400` or `404` depending on backend.
* DeleteProduct by another user (not `productAddedBy`) → expect `403` if authorization enforced.
* Re-run AddProduct many times — check for duplicate handling.

---

## Troubleshooting tips

* If `Authorization` returns `401` after login: ensure you are passing exact `token` returned (no extra quotes or `Bearer ` prefix unless required by your environment).
* If file upload fails: confirm the form field name is `productImage` and that your client sets `multipart/form-data` with correct boundary.
* If IDs look like MongoDB IDs (24 hex chars), ensure you don’t accidentally pass extra whitespace.

---
