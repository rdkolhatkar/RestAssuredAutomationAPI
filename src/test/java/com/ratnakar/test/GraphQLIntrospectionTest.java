package com.ratnakar.test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * ─────────────────────────────────────────────────────────────
 *  RUN THIS TEST FIRST — before writing any other test
 * ─────────────────────────────────────────────────────────────
 *
 *  It fires a GraphQL introspection query at the live server and
 *  prints the REAL schema: every Query field, every Mutation field,
 *  every type and its fields, and all argument names + types.
 *
 *  Read the console output carefully — it tells you:
 *    ✅ The exact mutation field name  (e.g. addCourse / createCourse / ...)
 *    ✅ The exact argument names       (e.g. courseTitle / courseName / ...)
 *    ✅ The exact argument types       (e.g. String / Int / ID / ...)
 *    ✅ Whether args use an input type (CourseInput) or are inline scalars
 *    ✅ The exact query field names    (e.g. courses / getCourse / allCourses / ...)
 *
 *  Once you know the real schema, update your JSON payload files
 *  under src/test/resources/data/ accordingly.
 *
 *  Payload file: src/test/resources/data/introspection.json
 */
public class GraphQLIntrospectionTest {

    private static final String BASE_URL = "https://rahulshettyacademy.com";
    private static final String GQL_PATH = "/gq/graphql";
    private static final String DATA_DIR = "GraphQLJsonQuery/";

    private RequestSpecification requestSpec;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI  = BASE_URL;
        RestAssured.basePath = GQL_PATH;

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }

    // =========================================================================
    // Introspection — prints the full schema to the console
    // =========================================================================
    @Test(description = "Discover real schema via GraphQL introspection")
    public void discoverSchema() {

        String body = loadPayload("introspection.json");

        Response response = given()
                .spec(requestSpec)
                .body(body)
            .when()
                .post()
            .then()
                .statusCode(200)
                .extract().response();

        // ── QUERY fields ─────────────────────────────────────────────────────
        System.out.println("\n▶ QUERY TYPE FIELDS:");
        List<Map<String, Object>> queryFields = response.jsonPath()
                .getList("data.__schema.queryType.fields");
        printFields(queryFields);

        // ── MUTATION fields ───────────────────────────────────────────────────
        System.out.println("\n▶ MUTATION TYPE FIELDS:");
        List<Map<String, Object>> mutationFields = response.jsonPath()
                .getList("data.__schema.mutationType.fields");
        printFields(mutationFields);

        // ── ALL TYPES ─────────────────────────────────────────────────────────
        System.out.println("\n▶ ALL OBJECT TYPES AND THEIR FIELDS:");
        List<Map<String, Object>> allTypes = response.jsonPath()
                .getList("data.__schema.types");

        for (Map<String, Object> type : allTypes) {
            String name = (String) type.get("name");
            String kind = (String) type.get("kind");

            // Skip built-in introspection types
            if (name != null && !name.startsWith("__") && kind != null
                    && (kind.equals("OBJECT") || kind.equals("INPUT_OBJECT") || kind.equals("ENUM"))) {

                System.out.println("\n  TYPE: " + name + "  [" + kind + "]");

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> fields = (List<Map<String, Object>>) type.get("fields");
                if (fields != null) {
                    for (Map<String, Object> field : fields) {
                        String fieldName = (String) field.get("name");
                        @SuppressWarnings("unchecked")
                        Map<String, Object> fieldType = (Map<String, Object>) field.get("type");
                        System.out.println("    - " + fieldName + " : " + resolveTypeName(fieldType));
                    }
                }
            }
        }

        System.out.println("\n\n  ✅ Copy the mutation name, argument names, and types shown above");
        System.out.println("     into your JSON payload files under src/test/resources/data/");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private void printFields(List<Map<String, Object>> fields) {
        if (fields == null || fields.isEmpty()) {
            System.out.println("  (none found)");
            return;
        }
        for (Map<String, Object> field : fields) {
            String name = (String) field.get("name");
            List<Map<String, Object>> args = (List<Map<String, Object>>) field.get("args");

            System.out.println("\n  field: " + name);
            if (args != null && !args.isEmpty()) {
                System.out.println("  args:");
                for (Map<String, Object> arg : args) {
                    String argName = (String) arg.get("name");
                    Map<String, Object> argType = (Map<String, Object>) arg.get("type");
                    System.out.println("    - " + argName + " : " + resolveTypeName(argType));
                }
            } else {
                System.out.println("  args: (none)");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private String resolveTypeName(Map<String, Object> typeNode) {
        if (typeNode == null) return "unknown";
        String kind = (String) typeNode.get("kind");
        String name = (String) typeNode.get("name");
        Map<String, Object> ofType = (Map<String, Object>) typeNode.get("ofType");

        if ("NON_NULL".equals(kind)) return resolveTypeName(ofType) + "!";
        if ("LIST".equals(kind))     return "[" + resolveTypeName(ofType) + "]";
        return name != null ? name : "unknown";
    }

    private String loadPayload(String fileName) {
        String path = DATA_DIR + fileName;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null)
                throw new RuntimeException("File not found on classpath: " + path);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read: " + path, e);
        }
    }
}
