package com.ratnakar.test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

 /**
 * GraphQL Course API — End-to-End Test Suite
 *
 * Endpoint   : https://rahulshettyacademy.com/gq/graphql
 * Payload files (src/test/resources/data/):
 *   - addCourse.json
 *   - getAllCourses.json
 *   - getCourseById.json       → contains {{courseId}} placeholder
 *   - addCourseMissingField.json
 *
 * Test flow:
 *   TC_01  addCourse mutation          → creates a course, captures ID
 *   TC_02  getAllCourses query          → list is non-empty, new course present
 *   TC_03  getCourseById query          → exact course returned by ID
 *   TC_04  addCourse (missing field)   → GraphQL errors block must be present
 */
public class AutomateGraphQLAPI {

    // ── Endpoint ──────────────────────────────────────────────────────────────
    private static final String BASE_URL = "https://rahulshettyacademy.com";
    private static final String GQL_PATH = "/gq/graphql";

    // ── Resource folder for payload JSON files ────────────────────────────────
    private static final String DATA_DIR = "GraphQLJsonQuery/";

    // ── Placeholder inside getCourseById.json ─────────────────────────────────
    private static final String PLACEHOLDER_ID = "{{courseId}}";

    // ── GPath expressions — match the ACTUAL field names from the server ───────
    private static final String PATH_COURSE_ID       = "data.createCourse.id";
    private static final String PATH_COURSE_NAME     = "data.createCourse.courseName";
    private static final String PATH_COURSE_PRICE    = "data.createCourse.price";
    private static final String PATH_COURSE_CATEGORY = "data.createCourse.category";
    private static final String PATH_COURSE_LANGUAGE = "data.createCourse.language";
    private static final String PATH_COURSE_EMAIL    = "data.createCourse.email";
    private static final String PATH_COURSE_STACK    = "data.createCourse.stack";

    // ── Expected values — must match variables inside addCourse.json ──────────
    private static final String EXPECTED_NAME     = "RestAssured + TestNG Mastery";
    private static final int    EXPECTED_PRICE    = 40;             // Int not Float
    private static final String EXPECTED_CATEGORY = "API Testing";
    private static final String EXPECTED_LANGUAGE = "Java";
    private static final String EXPECTED_EMAIL    = "test@rahulshettyacademy.com";
    private static final String EXPECTED_STACK    = "WEB";

    // ── Shared state ──────────────────────────────────────────────────────────
    private RequestSpecification  requestSpec;
    private ResponseSpecification responseSpec;
    private String createdCourseId;   // captured in TC_01, used in TC_02 & TC_03

    // ── One-time setup ────────────────────────────────────────────────────────
    @BeforeClass
    public void setup() {
        RestAssured.baseURI  = BASE_URL;
        RestAssured.basePath = GQL_PATH;

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)        // GraphQL always returns HTTP 200
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    // =========================================================================
    // TC_01 — MUTATION : createCourse
    // File  : src/test/resources/data/addCourse.json
    //
    // Uses named variable $input of type CourseInput!
    // price is Int (not Float), field is courseName (not courseTitle)
    // =========================================================================
    @Test(
            description = "TC_01 | createCourse mutation — create a course and assert all returned fields",
            priority    = 1
    )
    public void tc01_createCourse_Mutation() {

        String requestBody = loadPayload("addCourse.json");

        Response response = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post()
                .then()
                .spec(responseSpec)
                .body("errors",              nullValue())
                .body(PATH_COURSE_ID,       notNullValue())
                .body(PATH_COURSE_NAME,     equalTo(EXPECTED_NAME))
                .body(PATH_COURSE_PRICE,    equalTo(EXPECTED_PRICE))
                .body(PATH_COURSE_CATEGORY, equalTo(EXPECTED_CATEGORY))
                .body(PATH_COURSE_LANGUAGE, equalTo(EXPECTED_LANGUAGE))
                .body(PATH_COURSE_EMAIL,    equalTo(EXPECTED_EMAIL))
                .body(PATH_COURSE_STACK,    equalTo(EXPECTED_STACK))
                .extract().response();

        createdCourseId = response.jsonPath().getString(PATH_COURSE_ID);

        Assert.assertNotNull(createdCourseId,
                "Course ID from createCourse mutation must not be null");

        System.out.println("\n[TC_01] ✅ Course created | ID = " + createdCourseId);
    }

    // =========================================================================
    // TC_02 — QUERY : courses (get all)
    // File  : src/test/resources/data/getAllCourses.json
    // =========================================================================
    @Test(
            description = "TC_02 | courses query — list is non-empty and contains the newly created course",
            priority    = 2,
            dependsOnMethods = "tc01_createCourse_Mutation"
    )
    public void tc02_getAllCourses_Query() {

        String requestBody = loadPayload("getAllCourses.json");

        Response response = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post()
                .then()
                .spec(responseSpec)
                .body("errors",                  nullValue())
                .body("data.courses",            notNullValue())
                .body("data.courses.size()",     greaterThan(0))
                .body("data.courses.id",         everyItem(notNullValue()))
                .body("data.courses.courseName", everyItem(notNullValue()))
                .extract().response();

        List<String> allIds = response.jsonPath().getList("data.courses.id", String.class);

        Assert.assertTrue(
                allIds.contains(createdCourseId),
                "Course list must contain the ID created in TC_01: " + createdCourseId
        );

        System.out.println("\n[TC_02] ✅ courses query returned " + allIds.size()
                + " course(s). Created course found in list.");
    }

    // =========================================================================
    // TC_03 — QUERY : getCourse by ID
    // File  : src/test/resources/data/getCourseById.json
    //         {{courseId}} replaced at runtime with the ID from TC_01
    // =========================================================================
    @Test(
            description = "TC_03 | getCourse query — exact course returned by ID, all fields verified",
            priority    = 3,
            dependsOnMethods = "tc01_createCourse_Mutation"
    )
    public void tc03_getCourseById_Query() {

        String requestBody = loadPayload(
                "getCourseById.json",
                Map.of(PLACEHOLDER_ID, createdCourseId)
        );

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post()
                .then()
                .spec(responseSpec)
                .body("errors",                    nullValue())
                .body("data.getCourse.id",         equalTo(createdCourseId))
                .body("data.getCourse.courseName", equalTo(EXPECTED_NAME))
                .body("data.getCourse.price",      equalTo(EXPECTED_PRICE))
                .body("data.getCourse.category",   equalTo(EXPECTED_CATEGORY))
                .body("data.getCourse.language",   equalTo(EXPECTED_LANGUAGE))
                .body("data.getCourse.email",      equalTo(EXPECTED_EMAIL))
                .body("data.getCourse.stack",      equalTo(EXPECTED_STACK));

        System.out.println("\n[TC_03] ✅ getCourse returned correct course for ID = " + createdCourseId);
    }

    // =========================================================================
    // TC_04 — NEGATIVE : createCourse with missing required field (courseName)
    // File  : src/test/resources/data/addCourseMissingField.json
    //
    // courseName is String! (non-null) in CourseInput — omitting it must
    // produce a GraphQL validation error. HTTP stays 200 per GraphQL spec.
    // =========================================================================
    @Test(
            description = "TC_04 | Negative — createCourse without courseName must return GraphQL errors",
            priority    = 4
    )
    public void tc04_createCourse_MissingField_ShouldFail() {

        String requestBody = loadPayload("addCourseMissingField.json");

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(200)                          // GraphQL wraps errors in 200
                .body("errors",           notNullValue())  // errors array must exist
                .body("errors.size()",    greaterThan(0))
                .body("data.createCourse", nullValue())    // operation must not have succeeded
                .log().body();

        System.out.println("\n[TC_04] ✅ GraphQL validation errors returned for missing courseName — as expected.");
    }

    // =========================================================================
    // Helpers — load JSON files from src/test/resources/data/
    // =========================================================================

    /** Reads a file from the data/ folder on the classpath as-is. */
    private String loadPayload(String fileName) {
        String path = DATA_DIR + fileName;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null)
                throw new RuntimeException(
                        "Payload file not found: " + path +
                                "\nCheck that the file exists under src/test/resources/data/");
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read payload file: " + path, e);
        }
    }

    /** Reads a file and replaces each placeholder key with its runtime value. */
    private String loadPayload(String fileName, Map<String, String> replacements) {
        String content = loadPayload(fileName);
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue());
        }
        return content;
    }
}
/*
===============================================================================
HOW GRAPHQL QUERY IS SENT AS JSON & EXECUTED AT RUNTIME
===============================================================================

1. WHY JSON?
-------------
GraphQL APIs over HTTP expect the request body in JSON format.
We DO NOT send raw GraphQL query directly.

Standard format:
{
  "query": "GraphQL query/mutation as STRING",
  "variables": { key-value pairs }
}

-------------------------------------------------------------------------------

2. WHAT OUR CODE DOES
----------------------
- We store GraphQL queries in JSON files (addCourse.json, etc.)
- loadPayload() reads file as String
- RestAssured sends it as HTTP POST body

Example:
given()
   .body(requestBody)
   .post();

-------------------------------------------------------------------------------

3. WHAT HAPPENS ON GRAPHQL SERVER
---------------------------------
When server receives request:

Step 1: Parse JSON body
Step 2: Extract:
        - "query"      → actual GraphQL query
        - "variables"  → dynamic input values

Step 3: Convert into executable GraphQL operation

Example internally:
mutation($input: CourseInput!) {
  createCourse(input: $input) {
    id
    courseName
  }
}

Variables:
{
  "input": {
    "courseName": "RestAssured + TestNG Mastery",
    "price": 40
  }
}

-------------------------------------------------------------------------------

4. GRAPHQL EXECUTION ENGINE
---------------------------
- Validates schema (types, required fields)
- Resolves fields using backend logic
- Fetches/creates data
- Builds response

-------------------------------------------------------------------------------

5. RESPONSE FORMAT
------------------
GraphQL ALWAYS returns JSON:

Success:
{
  "data": { ... }
}

Failure:
{
  "errors": [ ... ]
}

NOTE:
- HTTP status is usually 200 (even for errors)
- Errors are inside "errors" field

-------------------------------------------------------------------------------

6. WHY PLACEHOLDERS ({{courseId}})
-----------------------------------
We replace placeholders at runtime:

content.replace("{{courseId}}", actualId);

This allows dynamic query execution without modifying JSON files.

-------------------------------------------------------------------------------

SUMMARY
--------
JSON (client)  → GraphQL query extracted → Executed → JSON response returned

===============================================================================
*/