# 🔐 OAuth 2.0 — Complete Guide (Beginner to Advanced)

> A detailed, easy-to-understand guide covering all theoretical concepts of OAuth and OAuth 2.0 — written for freshers and junior developers.

---

## 📚 Table of Contents

1. [What is OAuth? — The Big Picture](#1-what-is-oauth--the-big-picture)
2. [Why Was OAuth Invented?](#2-why-was-oauth-invented)
3. [OAuth 1.0 vs OAuth 2.0 — What Changed?](#3-oauth-10-vs-oauth-20--what-changed)
4. [Core Concepts & Terminology](#4-core-concepts--terminology)
   - [Client-Side Terminologies](#-client-side-terminologies)
   - [Server-Side Terminologies](#-server-side-terminologies)
5. [The OAuth 2.0 Flow — Step by Step](#5-the-oauth-20-flow--step-by-step)
6. [Grant Types Explained](#6-grant-types-explained)
7. [Understanding Tokens](#7-understanding-tokens)
8. [Scopes — Controlling Access](#8-scopes--controlling-access)
9. [How to Pass OAuth in API Requests](#9-how-to-pass-oauth-in-api-requests)
10. [Real-World Contract Breakdown](#10-real-world-contract-breakdown)
11. [Step-by-Step API Requests](#11-step-by-step-api-requests)
12. [Security Best Practices](#12-security-best-practices)
13. [Common Mistakes Freshers Make](#13-common-mistakes-freshers-make)
14. [Quick Reference Cheat Sheet](#14-quick-reference-cheat-sheet)

---

## 1. What is OAuth? — The Big Picture

**OAuth** stands for **Open Authorization**.

Think of OAuth as a **valet key** for your car. When you go to a restaurant and hand your car to the valet, you give them a *special valet key* — it can start the car and drive it to the parking lot, but it cannot open the glove compartment or the trunk. You are giving **limited access** without handing over your **master key**.

Similarly, OAuth allows an application to access your data on another service **without you giving your password** to that application.

### 🧠 Real-Life Analogy

You use a third-party app like **Spotify** and click **"Login with Google"**.

- Spotify **never** gets your Google password.
- Google asks you: *"Do you want to allow Spotify to access your email address?"*
- You click **Allow**.
- Google gives Spotify a **temporary token** to access only what you approved.

This entire process is powered by **OAuth 2.0**.

---

## 2. Why Was OAuth Invented?

Before OAuth, apps used a method called **"Password Anti-Pattern"**:

> *"Give us your username and password and we will log in on your behalf."*

### Problems with the Password Anti-Pattern:
- The third-party app had **full access** to your account.
- If the app was hacked, your **password was stolen**.
- You could not **revoke access** without changing your password.
- The app could do **anything** — delete files, read private emails, etc.

### OAuth Solution:
OAuth replaces the password with a **token** (a long random string). This token:
- Has a **limited lifetime** (expires).
- Can be **revoked** at any time without changing your password.
- Grants only **specific permissions** (called Scopes).

---

## 3. OAuth 1.0 vs OAuth 2.0 — What Changed?

| Feature | OAuth 1.0 | OAuth 2.0 |
|---|---|---|
| Complexity | Very complex | Simpler |
| Token Signing | Required cryptographic signing | No signing needed |
| Transport Security | Could work without HTTPS | **Requires HTTPS** |
| Token Types | Only one type | Access Token + Refresh Token |
| Support for Mobile Apps | Poor | Excellent |
| Industry Adoption | Declining | **Standard today** |

> ✅ **OAuth 2.0 is the industry standard** used by Google, Facebook, GitHub, Twitter, and virtually every modern API.

---

## 4. Core Concepts & Terminology

### 🖥️ Client-Side Terminologies

These are terms related to the **application (client)** that is requesting access.

---

#### 🔹 Client (Application)

The **client** is the application that wants to access the user's data. It could be:
- A mobile app (e.g., Instagram)
- A web application (e.g., an online tool that reads your Google Drive)
- A backend server making API calls

**Example from contract:**
> The application using the OAuth flow is the client requesting access to Google's APIs.

---

#### 🔹 Client ID

The **Client ID** is a **public identifier** for your application. It is like your application's **username** in the OAuth world. It is not secret — it can appear in URLs.

**Example from contract:**
```
Client ID: 692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com
```

Every time you register an application with Google (or any OAuth provider), you get a unique Client ID. This ID tells the authorization server: *"Hey, it's this specific application making the request."*

---

#### 🔹 Client Secret

The **Client Secret** is a **confidential password** known only to the client application and the authorization server. It must **never be exposed** in front-end code or URLs.

**Example from contract:**
```
Client Secret: erZOWM9g3UtwNRj340YYaK_W
```

> ⚠️ **Warning for Freshers:** Never commit your Client Secret to GitHub or expose it in browser-side JavaScript. This is like sharing your banking password!

---

#### 🔹 Redirect URI (Callback URL)

The **Redirect URI** (also called Callback URL) is the address where the authorization server sends the user **after they approve or deny** the access request.

**Example from contract:**
```
Redirect URL: https://rahulshettyacademy.com/getCourse.php
```

Think of it as: *"After the user logs in with Google, bring them back to this page of my website."*

> ⚠️ **Security Note:** The redirect URI must be **pre-registered** with the authorization server. If a malicious app tries to redirect to a different URL, the server will reject it.

---

#### 🔹 Scope

**Scope** defines **what permissions** your application is requesting. It tells the server: *"My app only needs to read your email — nothing more."*

**Example from contract:**
```
Scope: https://www.googleapis.com/auth/userinfo.email
```

This scope asks Google for **only the email address** of the user. Common scopes include:
- `userinfo.email` — Read the user's email address
- `userinfo.profile` — Read the user's name and profile picture
- `drive.readonly` — Read files from Google Drive (but not edit)
- `calendar` — Read and write Google Calendar events

**Multiple scopes** can be requested at once, separated by spaces:
```
scope=userinfo.email userinfo.profile
```

---

#### 🔹 State

The **State** parameter is a **random string** generated by the client to protect against **CSRF (Cross-Site Request Forgery) attacks**.

**Example from contract:**
```
State: Any random string (e.g., "xK92mP3qLz")
```

**How State works:**
1. Client generates a random string: `"abc123xyz"`
2. Client sends it to the authorization server.
3. Authorization server sends it back in the redirect.
4. Client checks: *"Is the state I got back the same one I sent?"*
5. If not matching → **attack detected** → request rejected.

---

#### 🔹 Response Type

The **Response Type** tells the authorization server **what to return** after the user approves access.

```
response_type=code
```

In the **Authorization Code** flow (the most secure), the server returns a **short-lived code** (not the token directly). This code is then exchanged for an actual access token.

---

#### 🔹 Grant Type

The **Grant Type** tells the authorization server **which OAuth 2.0 flow** the client is using.

**Example from contract:**
```
Grant Type: Authorization Code
```

| Grant Type | Use Case |
|---|---|
| Authorization Code | Web apps, most secure |
| Client Credentials | Server-to-server (no user involved) |
| Implicit | Old browser-only apps (deprecated) |
| Password | Trusted first-party apps only (avoid) |

---

### 🖧 Server-Side Terminologies

These are terms related to the **servers** that handle OAuth on the provider's side (e.g., Google's servers).

---

#### 🔹 Resource Owner

The **Resource Owner** is the **end user** — the human being who owns the data and has the power to grant or deny access to it.

**Example:** You are the Resource Owner of your Gmail inbox, Google Drive files, and profile data.

---

#### 🔹 Authorization Server

The **Authorization Server** is the server that **authenticates the user** and **issues tokens**. This is the server you redirect users to so they can log in and approve access.

**Example from contract:**
```
Authorization Server URL: https://accounts.google.com/o/oauth2/v2/auth
```

When your app redirects to this URL, Google shows the user a login page and a consent screen asking:
> *"This app wants to access your email address. Allow or Deny?"*

The Authorization Server is responsible for:
- Verifying the user's identity (authentication).
- Showing the consent screen.
- Issuing the **Authorization Code**.

---

#### 🔹 Resource Server

The **Resource Server** is the server that **hosts the actual protected data**. After the client gets a valid access token, it calls the Resource Server to get the user's data.

**Example:** Google's API servers like:
- `https://www.googleapis.com/userinfo/v2/me` — Returns user profile

> 📌 Note: Sometimes the Authorization Server and Resource Server are the **same server**, but conceptually they serve different roles.

---

#### 🔹 Access Token URL (Token Endpoint)

The **Access Token URL** is the endpoint where the client sends the **Authorization Code** (plus Client ID, Client Secret, etc.) to receive an **Access Token** in return.

**Example from contract:**
```
Access Token URL: https://www.googleapis.com/oauth2/v4/token
```

This is a **server-to-server** call — it happens in the backend and is never visible to the browser.

---

#### 🔹 Authorization Code

The **Authorization Code** is a **short-lived, one-time-use code** that the Authorization Server gives to the client after the user logs in and approves access.

- It is valid for only **a few minutes** (usually 60–600 seconds).
- It can only be used **once**.
- It is exchanged at the Token Endpoint for an actual Access Token.

Think of the Authorization Code as a **claim ticket** at a coat check. You give the ticket to the counter and get your actual coat (the access token) back.

---

#### 🔹 Access Token

The **Access Token** is the **main credential** used to call protected APIs. It proves that the client has been authorized to access the user's data.

- It is a **long random string** (usually a JWT or opaque token).
- It has an **expiry time** (e.g., 1 hour).
- It is sent in **every API request** as a header.

**Example:**
```
Authorization: Bearer ya29.A0ARrdaM-...
```

---

#### 🔹 Refresh Token

The **Refresh Token** is a **long-lived token** used to get a **new Access Token** after the old one expires — without making the user log in again.

- Access Tokens expire quickly (minutes/hours).
- Refresh Tokens can last days, weeks, or months.
- Stored securely on the server (never in the browser).

**Lifecycle:**
```
Access Token expires → Use Refresh Token → Get new Access Token
```

---

#### 🔹 Bearer Token

**Bearer** means *"whoever holds this token has access"*. The token itself is the credential — just like cash. If someone steals your bearer token, they can use it.

This is why access tokens must always be sent over **HTTPS** and stored securely.

---

## 5. The OAuth 2.0 Flow — Step by Step

Below is the **Authorization Code Grant** flow (the one in the contract):

```
USER            CLIENT APP           AUTHORIZATION SERVER     RESOURCE SERVER
  |                  |                        |                      |
  |---[1] Click "Login with Google"--->|      |                      |
  |                  |                        |                      |
  |                  |---[2] Redirect to Auth Server (with client_id, scope, redirect_uri, state)-->|
  |                  |                        |                      |
  |<---------[3] Google Login & Consent Screen shown----------------|
  |                  |                        |                      |
  |---[4] User Approves Access-------------->|                      |
  |                  |                        |                      |
  |                  |<--[5] Redirect back with Authorization Code---|
  |                  |                        |                      |
  |                  |---[6] POST: code + client_id + client_secret + redirect_uri-->|
  |                  |                        |                      |
  |                  |<--[7] Access Token (+ Refresh Token)---------|
  |                  |                        |                      |
  |                  |---[8] GET /userinfo (Authorization: Bearer <token>)---------->|
  |                  |                        |                      |
  |                  |<--[9] Protected Resource (email, profile, etc.)--------------|
```

### Plain English Explanation of Each Step:

| Step | Who Does It | What Happens |
|---|---|---|
| 1 | User | Clicks "Login with Google" on your app |
| 2 | Client App | Redirects browser to Google's login page |
| 3 | Auth Server | Google shows login form and consent screen |
| 4 | User | Approves the requested permissions |
| 5 | Auth Server | Redirects back to your app with a short-lived `code` |
| 6 | Client App | Sends `code` + `client_secret` to Google's token endpoint (backend call) |
| 7 | Auth Server | Returns the `access_token` (and optionally a `refresh_token`) |
| 8 | Client App | Calls Google's API with the `access_token` |
| 9 | Resource Server | Returns the requested data (email, profile, etc.) |

---

## 6. Grant Types Explained

### 🔸 Authorization Code (Used in Contract)

**Best for:** Web applications and mobile apps.

This is the **most secure and widely recommended** flow. The access token is never exposed in the browser URL. The exchange of the authorization code for a token happens **server-to-server**.

```
Request 1: Get Authorization Code
Request 2: Exchange Code for Access Token
```

---

### 🔸 Client Credentials

**Best for:** Machine-to-machine (M2M) communication. No user is involved.

**Example:** A backend cron job that reads data from a third-party API every night. There is no human user logging in.

```
Client sends: client_id + client_secret + grant_type=client_credentials
Server returns: access_token
```

---

### 🔸 Implicit (Deprecated)

**Best for:** (Was used for) Single-page browser apps in 2012.

The access token was returned directly in the browser URL fragment — very insecure. This grant type is **deprecated** in OAuth 2.1.

---

### 🔸 Resource Owner Password Credentials (ROPC)

**Best for:** Only for highly trusted first-party apps (e.g., the service's own mobile app).

The user gives their username and password **directly to the client app**, which sends them to the server. This defeats much of OAuth's purpose and should be **avoided**.

---

## 7. Understanding Tokens

### Token Anatomy (JWT Example)

A common format for access tokens is **JWT (JSON Web Token)**. It has three parts separated by dots:

```
eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiZW1haWwiOiJ1c2VyQGV4YW1wbGUuY29tIiwiZXhwIjoxNzE5MDAwMDAwfQ.signature
      HEADER                              PAYLOAD                                SIGNATURE
```

**Decoded Payload:**
```json
{
  "sub": "1234567890",
  "email": "user@example.com",
  "exp": 1719000000
}
```

- `sub` — Subject (User ID)
- `email` — The user's email
- `exp` — Expiry timestamp (Unix time)

### Token Comparison

| | Access Token | Refresh Token | Authorization Code |
|---|---|---|---|
| **Purpose** | Call APIs | Get new access token | Exchange for tokens |
| **Lifetime** | Short (1 hour) | Long (days/months) | Very short (1–10 min) |
| **Where Stored** | Memory or secure storage | Server-side or secure storage | Temporarily in redirect URL |
| **Can be reused?** | Yes (until expiry) | Yes (until revoked) | No (one-time use) |

---

## 8. Scopes — Controlling Access

Scopes are the **permission system** of OAuth. They define exactly what your application can and cannot do with the user's account.

### How Google Scopes Work

**From the contract:**
```
Scope: https://www.googleapis.com/auth/userinfo.email
```

This scope grants permission to **read the user's email address only**. Other Google scopes include:

| Scope | Access Granted |
|---|---|
| `userinfo.email` | Read email address |
| `userinfo.profile` | Read name, photo, locale |
| `drive` | Full read/write access to Google Drive |
| `drive.readonly` | Read-only access to Google Drive |
| `calendar.events` | Read and write calendar events |
| `gmail.readonly` | Read-only access to Gmail |

### Principle of Least Privilege

> Always request the **minimum scopes needed**. Never ask for `drive` if you only need `drive.readonly`.

This is not just good security practice — it also increases user trust. Users are less likely to approve an app that requests too many permissions.

---

## 9. How to Pass OAuth in API Requests

**From the contract:**
```
How to pass OAuth in request: Headers
```

The access token is passed in the **HTTP Authorization header** using the `Bearer` scheme:

```http
GET /userinfo/v2/me HTTP/1.1
Host: www.googleapis.com
Authorization: Bearer ya29.A0ARrdaM_abc123...
```

### Why Not in Query Parameters?

Some older APIs allow passing the token in the URL:
```
https://api.example.com/data?access_token=abc123
```

This is **NOT recommended** because:
- URLs are logged in server logs and browser history.
- Tokens in URLs can be leaked via the `Referer` header.
- **Always use the Authorization header** — it is not logged by default.

---

## 10. Real-World Contract Breakdown

Let's break down every field in the OAuth contract you were given:

```
+----------------------------+---------------------------------------------------------------+
| Field                      | Value & Meaning                                               |
+----------------------------+---------------------------------------------------------------+
| Grant Type                 | Authorization Code                                            |
|                            | → The most secure OAuth 2.0 flow, uses a two-step process:   |
|                            |   first get a code, then exchange it for a token.            |
+----------------------------+---------------------------------------------------------------+
| Redirect URL/Callback URL  | https://rahulshettyacademy.com/getCourse.php                 |
|                            | → After user approves access, Google redirects here          |
|                            |   with the authorization code in the URL.                    |
+----------------------------+---------------------------------------------------------------+
| Authorization Server URL   | https://accounts.google.com/o/oauth2/v2/auth                 |
|                            | → Step 1: Your app sends the user to this URL to log in      |
|                            |   and approve access.                                        |
+----------------------------+---------------------------------------------------------------+
| Access Token URL           | https://www.googleapis.com/oauth2/v4/token                   |
|                            | → Step 2: After getting the code, your backend sends a POST  |
|                            |   here to exchange the code for an access token.             |
+----------------------------+---------------------------------------------------------------+
| Client ID                  | 692183103107-p0m7ent2...apps.googleusercontent.com           |
|                            | → Public identifier of your registered application.          |
+----------------------------+---------------------------------------------------------------+
| Client Secret              | erZOWM9g3UtwNRj340YYaK_W                                     |
|                            | → Secret key to authenticate your app to Google.             |
|                            |   NEVER expose this in client-side code.                     |
+----------------------------+---------------------------------------------------------------+
| Scope                      | https://www.googleapis.com/auth/userinfo.email               |
|                            | → Only requesting the user's email address — nothing more.   |
+----------------------------+---------------------------------------------------------------+
| State                      | Any random string                                            |
|                            | → CSRF protection. Generate a unique string per request      |
|                            |   and verify it when the callback comes back.                |
+----------------------------+---------------------------------------------------------------+
| How to pass OAuth          | Headers                                                       |
|                            | → Send the access token as: Authorization: Bearer <token>    |
+----------------------------+---------------------------------------------------------------+
```

---

## 11. Step-by-Step API Requests

The contract defines two mandatory request sequences. Here they are in full detail:

---

### 🔷 Request 1: Get Authorization Code

**Endpoint:** Authorization Server URL  
**Method:** `GET` (browser redirect)

**Required Query Parameters:**

| Parameter | Value | Description |
|---|---|---|
| `scope` | `https://www.googleapis.com/auth/userinfo.email` | What permission is being requested |
| `auth_url` | `https://accounts.google.com/o/oauth2/v2/auth` | The authorization server URL |
| `client_id` | `692183103107-p0m7...` | Your app's identifier |
| `response_type` | `code` | Tells server to return a code |
| `redirect_uri` | `https://rahulshettyacademy.com/getCourse.php` | Where to redirect after approval |

**Full URL:**
```
https://accounts.google.com/o/oauth2/v2/auth
  ?scope=https://www.googleapis.com/auth/userinfo.email
  &client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com
  &response_type=code
  &redirect_uri=https://rahulshettyacademy.com/getCourse.php
  &state=randomstring123
```

**Output (comes back in the redirect URL):**
```
https://rahulshettyacademy.com/getCourse.php?code=4/0AX4XfWi...&state=randomstring123
```

The `code` value is your **Authorization Code**. Grab it and use it in Request 2.

---

### 🔷 Request 2: Get Access Token

**Endpoint:** Access Token URL  
**Method:** `POST`

**Required Query Parameters / Body:**

| Parameter | Value | Description |
|---|---|---|
| `code` | `4/0AX4XfWi...` | The authorization code from Request 1 |
| `client_id` | `692183103107-p0m7...` | Your app's identifier |
| `client_secret` | `erZOWM9g3UtwNRj340YYaK_W` | Your app's secret |
| `redirect_uri` | `https://rahulshettyacademy.com/getCourse.php` | Must match the one used in Request 1 |
| `grant_type` | `authorization_code` | The type of OAuth flow being used |

**POST Request:**
```http
POST https://www.googleapis.com/oauth2/v4/token
Content-Type: application/x-www-form-urlencoded

code=4/0AX4XfWi...
&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com
&client_secret=erZOWM9g3UtwNRj340YYaK_W
&redirect_uri=https://rahulshettyacademy.com/getCourse.php
&grant_type=authorization_code
```

**Output (JSON response):**
```json
{
  "access_token": "ya29.A0ARrdaM_...",
  "expires_in": 3599,
  "refresh_token": "1//0gLn...",
  "scope": "https://www.googleapis.com/auth/userinfo.email",
  "token_type": "Bearer"
}
```

Now use the `access_token` to call protected APIs!

---

### 🔷 Request 3: Call the Protected API

```http
GET https://www.googleapis.com/userinfo/v2/me
Authorization: Bearer ya29.A0ARrdaM_...
```

**Response:**
```json
{
  "id": "10769150350006150715",
  "email": "user@gmail.com",
  "verified_email": true,
  "name": "John Doe"
}
```

---

## 12. Security Best Practices

| Practice | Why It Matters |
|---|---|
| Always use **HTTPS** | Tokens sent over HTTP can be intercepted |
| **Never expose Client Secret** in frontend code | Anyone who gets it can impersonate your app |
| Use the **State parameter** | Prevents CSRF attacks |
| Request **minimum scopes** | Users trust apps more; reduces damage if token is stolen |
| **Store tokens securely** | Use httpOnly cookies or secure server-side storage |
| **Rotate refresh tokens** | If a refresh token is stolen, limit the damage |
| Use **short-lived access tokens** | If stolen, they expire quickly |
| **Validate redirect URIs** | Always pre-register redirect URIs with the OAuth provider |
| Never log access tokens | Token in logs = security breach |

---

## 13. Common Mistakes Freshers Make

### ❌ Mistake 1: Exposing Client Secret in Frontend JavaScript

```javascript
// ❌ WRONG — Client secret is visible to anyone who reads your JS code
const clientSecret = "erZOWM9g3UtwNRj340YYaK_W";
```

✅ Keep the Client Secret on your **backend server only**. The token exchange (Request 2) should always happen server-side.

---

### ❌ Mistake 2: Not Validating the State Parameter

Many developers skip state validation, leaving their app open to CSRF attacks. Always:
1. Generate a random `state` before redirecting.
2. Store it in the user's session.
3. Compare it with the `state` returned in the callback.

---

### ❌ Mistake 3: Storing Access Tokens in localStorage

```javascript
// ❌ WRONG — localStorage is accessible to any JavaScript on the page (XSS risk)
localStorage.setItem("access_token", token);
```

✅ Use **httpOnly cookies** or keep the token in memory (for SPAs).

---

### ❌ Mistake 4: Using the Same Authorization Code Twice

Authorization codes are **one-time-use**. If you try to use the same code again, the authorization server will reject it — and may also revoke the previously issued tokens as a security measure.

---

### ❌ Mistake 5: Not Handling Token Expiry

Access tokens expire (usually in 1 hour). Always handle the `401 Unauthorized` response and use your refresh token to get a new access token automatically.

---

## 14. Quick Reference Cheat Sheet

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        OAuth 2.0 CHEAT SHEET                                │
├──────────────────────────┬──────────────────────────────────────────────────┤
│ Term                     │ Simple Definition                                 │
├──────────────────────────┼──────────────────────────────────────────────────┤
│ OAuth 2.0                │ Authorization framework for delegated access      │
│ Client                   │ Your application                                  │
│ Client ID                │ Public app identifier (like a username)           │
│ Client Secret            │ Private app password (keep it secret!)            │
│ Resource Owner           │ The end user who owns the data                    │
│ Authorization Server     │ Server that logs in user and issues codes/tokens  │
│ Resource Server          │ Server that has the protected data (API)          │
│ Authorization Code       │ Short-lived code, exchanged for access token      │
│ Access Token             │ Key to call protected APIs                        │
│ Refresh Token            │ Used to get new access tokens without re-login    │
│ Scope                    │ Permission(s) being requested                     │
│ Redirect URI             │ Where user is sent after authorization            │
│ State                    │ Random string to prevent CSRF                     │
│ Grant Type               │ The OAuth flow being used                         │
│ Bearer Token             │ Token type — whoever holds it has access          │
│ JWT                      │ Common format for access tokens                   │
├──────────────────────────┼──────────────────────────────────────────────────┤
│ FLOW SUMMARY             │                                                   │
│ Step 1                   │ GET Auth Code ← Authorization Server              │
│ Step 2                   │ POST Auth Code → Get Access Token                 │
│ Step 3                   │ Use Access Token to call APIs                     │
└──────────────────────────┴──────────────────────────────────────────────────┘
```

---

## 📌 Summary

OAuth 2.0 is not just a technology — it is a **security mindset**. The entire framework is built on the principle of:

> **"Grant only the access that is needed, for only as long as it is needed, to only the party that needs it."**

As a fresher, here is your learning checklist:

- [x] Understand what OAuth 2.0 is and why it exists
- [x] Know the difference between Client ID and Client Secret
- [x] Understand what the Authorization Server and Resource Server do
- [x] Know the Authorization Code flow end-to-end
- [x] Understand what Scopes are and the principle of least privilege
- [x] Know how to pass a token in API requests (Authorization header)
- [x] Understand what Access Tokens and Refresh Tokens are
- [x] Be aware of common security mistakes to avoid

---

*Guide prepared based on OAuth 2.0 Contract Details — Google OAuth Integration*  
*Reference Contract: Grant Type = Authorization Code | Provider = Google*
