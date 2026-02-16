# 🚀 Jira API Setup & Integration Guide
### Project: Ratnakar Agile Works

This guide explains:

- How to create a free Jira Cloud account
- How to create a Scrum project
- How to generate a Jira REST API token
- How to integrate Jira API using:
   - Postman
   - cURL
   - Java (HttpClient)

---

# 📌 Part 1 — Create Free Jira Cloud Account

## Step 1: Sign Up

Go to:

👉 https://www.atlassian.com/software/jira/free

- Choose **Jira Software**
- Select **Free Plan**
- No credit card required
- Supports up to 10 users

After signup, your site will look like:

```
https://your-team.atlassian.net
```

---

# 📌 Part 2 — Create Scrum Project

## Create Project

1. Go to **Projects → Create Project**
2. Select:
   - **Software**
   - **Scrum**
   - Choose **Team-managed** (recommended)
3. Project Name:

```
Ratnakar Agile Works
```

4. Choose Project Key:
```
RAW
```

5. Click **Create**

Your project will now include:
- Backlog
- Scrum board
- Sprint planning
- Reports

---

# 📌 Part 3 — Generate Jira REST API Token

Jira Cloud uses **API Tokens** instead of passwords.

## Step 1: Open API Token Page

Go to:

👉 https://id.atlassian.com/manage-profile/security/api-tokens

## Step 2: Create Token

1. Click **Create API Token**
2. Give it a name (e.g., `JiraAPI`)
3. Click **Create**
4. Copy the generated token (save it securely)

⚠️ You will not be able to see it again.

---

# 📌 Part 4 — Jira REST API Authentication

Jira Cloud uses:

```
Basic Authentication
```

Where:

- Username = Your Atlassian email
- Password = API Token

Authorization Header:

```
Authorization: Basic Base64(email:api_token)
```

---

# 📌 Part 5 — Test Jira API Using cURL

## Example 1: Get All Projects

```bash
curl -u your-email@example.com:your_api_token \
  -X GET \
  -H "Accept: application/json" \
  https://your-team.atlassian.net/rest/api/3/project
```

---

## Example 2: Create an Issue

```bash
curl -u your-email@example.com:your_api_token \
  -X POST \
  -H "Content-Type: application/json" \
  https://your-team.atlassian.net/rest/api/3/issue \
  --data '{
    "fields": {
      "project": {
        "key": "RAW"
      },
      "summary": "Test issue created via API",
      "description": "Creating an issue using REST API",
      "issuetype": {
        "name": "Task"
      }
    }
  }'
```

---

# 📌 Part 6 — Test Jira API Using Postman

## Step 1: Create Request

- Method: `GET`
- URL:

```
https://your-team.atlassian.net/rest/api/3/project
```

## Step 2: Authorization Tab

- Type: **Basic Auth**
- Username: your email
- Password: API Token

## Step 3: Send Request

You should receive a JSON response listing projects.

---

# 📌 Part 7 — Java Integration Example

Below example uses Java 11+ HttpClient.

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class JiraApiExample {

    public static void main(String[] args) throws Exception {

        String email = "your-email@example.com";
        String apiToken = "your_api_token";
        String jiraUrl = "https://your-team.atlassian.net/rest/api/3/project";

        String auth = email + ":" + apiToken;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(jiraUrl))
                .header("Authorization", "Basic " + encodedAuth)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}
```

---

# 📌 Common API Endpoints

| Operation | Endpoint |
|-----------|----------|
| Get all projects | `/rest/api/3/project` |
| Get issue | `/rest/api/3/issue/{issueKey}` |
| Create issue | `/rest/api/3/issue` |
| Search issues | `/rest/api/3/search` |
| Get users | `/rest/api/3/users/search` |

Full Documentation:  
👉 https://developer.atlassian.com/cloud/jira/platform/rest/v3/

---

# 📌 Best Practices

- Never hardcode API tokens in production
- Store credentials in environment variables
- Use HTTPS always
- Rotate API tokens periodically

---

# 📌 Example Environment Variables

### Windows (PowerShell)

```powershell
setx JIRA_EMAIL "your-email@example.com"
setx JIRA_TOKEN "your_api_token"
```

### Mac/Linux

```bash
export JIRA_EMAIL="your-email@example.com"
export JIRA_TOKEN="your_api_token"
```

---

# 📌 Project Structure Example (Automation Framework)

```
jira-api-project/
│
├── src/
│   └── JiraApiExample.java
│
├── pom.xml
└── README.md
```

---

# 🎯 Final Outcome

After completing this guide, you will:

- Have a Jira Cloud account
- Have a Scrum project (Ratnakar Agile Works)
- Be able to authenticate via API token
- Create and fetch Jira issues via:
   - cURL
   - Postman
   - Java code

---

# 👨‍💻 Author

Ratnakar Kolhatkar  
Automation Engineer | API & UI Testing Enthusiast

---

⭐ If this guide helped you, consider starring the repository!
