package com.ratnakar.test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;

public class JiraAtlassianApiTest {

    String issueId = null;
    String attachmentName = null;
    String issueNumber = null;
    @Test
    public void JiraBugIssueCreationApiTest() throws IOException {

       RestAssured.baseURI = "https://ratnakarkolhatkar.atlassian.net";

        String requestBody = new String(Files.readAllBytes(
                Paths.get("src/test/resources/JsonData/JiraBugIssueCreationRequest.json")
        ));

        String createBugIssueResponse = given().header("Content-Type", "application/json")
                .header("Authorization", "Basic cmF0bmFrYXJrb2xoYXRrYXJAZ21haWwuY29tOkFUQVRUM3hGZkdGMGt5aUFTNUhiVkYyc2pTcE9vM3NrOTQwNzJSWEJJNEhFMk5qS3QtLTZ0bGJHVTc5Z3FVQUhEYXRndjB0Q0hoVGZDSldISVh1TXRYdDdzanE0LWJvWkpQR3VLTDhvV2p4N0FNdDY1ZmJmcWxlSy1YdjhZVVFqa016eWE5X2Z3YWgyaVNPM3Z3bHBiTDBWc1lvZTlPbU1NdUJsOFZNSzY4aE5vUDZXaXFxQURpST02NkRDOTU3Qw==")
                .body(requestBody)
                .log().all()
                .post("/rest/api/3/issue")
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().response().asString();

        JsonPath jsonPath = new JsonPath(createBugIssueResponse);
        // Extract issue id from the response
        issueId = jsonPath.getString("id");
        System.out.println("Atlassian Jira issue ID is : "+ issueId);
    }

    @Test
    public void JiraIssueAttachmentApiTest(){

        RestAssured.baseURI = "https://ratnakarkolhatkar.atlassian.net";

        String bugAttachmentResponse = given()
                .header("Content-Type", "multipart/form-data; boundary=<calculated when request is sent>")
                .header("X-Atlassian-Token", "no-check")
                .header("Authorization", "Basic cmF0bmFrYXJrb2xoYXRrYXJAZ21haWwuY29tOkFUQVRUM3hGZkdGMGt5aUFTNUhiVkYyc2pTcE9vM3NrOTQwNzJSWEJJNEhFMk5qS3QtLTZ0bGJHVTc5Z3FVQUhEYXRndjB0Q0hoVGZDSldISVh1TXRYdDdzanE0LWJvWkpQR3VLTDhvV2p4N0FNdDY1ZmJmcWxlSy1YdjhZVVFqa016eWE5X2Z3YWgyaVNPM3Z3bHBiTDBWc1lvZTlPbU1NdUJsOFZNSzY4aE5vUDZXaXFxQURpST02NkRDOTU3Qw==")
                .pathParam("key", issueId)
                .multiPart("file", new File("src/test/resources/ImageData/TicketBookingAPI_Error.PNG"))
                .log().all()
                .post("/rest/api/3/issue/{key}/attachments")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath jsonPath = new JsonPath(bugAttachmentResponse);
        // Extract issue attachment name from the response
        attachmentName = jsonPath.getString("filename");
        System.out.println("Atlassian Jira bug attachment ID is : "+ attachmentName);
        Assert.assertEquals("TicketBookingAPI_Error.PNG", attachmentName);
    }

    @Test
    public void getJiraIssueTest(){

        RestAssured.baseURI = "https://ratnakarkolhatkar.atlassian.net";

        String getIssueResponse = given().log().all()
                .header("Authorization", "Basic cmF0bmFrYXJrb2xoYXRrYXJAZ21haWwuY29tOkFUQVRUM3hGZkdGMGt5aUFTNUhiVkYyc2pTcE9vM3NrOTQwNzJSWEJJNEhFMk5qS3QtLTZ0bGJHVTc5Z3FVQUhEYXRndjB0Q0hoVGZDSldISVh1TXRYdDdzanE0LWJvWkpQR3VLTDhvV2p4N0FNdDY1ZmJmcWxlSy1YdjhZVVFqa016eWE5X2Z3YWgyaVNPM3Z3bHBiTDBWc1lvZTlPbU1NdUJsOFZNSzY4aE5vUDZXaXFxQURpST02NkRDOTU3Qw==")
                .header("Accept", "application/json")
                .pathParam("issueId", issueId)
                .log().all()
                .get("/rest/api/3/issue/{issueId}")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath jsonPath = new JsonPath(getIssueResponse);
        // Extract issue id from the response
        issueNumber = jsonPath.getString("id");
        System.out.println("Atlassian Jira issue number is : "+ issueNumber);
    }

    @Test
    public void deleteJiraIssueTest(){
        RestAssured.baseURI = "https://ratnakarkolhatkar.atlassian.net";
        given().log().all()
                .header("Authorization", "Basic cmF0bmFrYXJrb2xoYXRrYXJAZ21haWwuY29tOkFUQVRUM3hGZkdGMGt5aUFTNUhiVkYyc2pTcE9vM3NrOTQwNzJSWEJJNEhFMk5qS3QtLTZ0bGJHVTc5Z3FVQUhEYXRndjB0Q0hoVGZDSldISVh1TXRYdDdzanE0LWJvWkpQR3VLTDhvV2p4N0FNdDY1ZmJmcWxlSy1YdjhZVVFqa016eWE5X2Z3YWgyaVNPM3Z3bHBiTDBWc1lvZTlPbU1NdUJsOFZNSzY4aE5vUDZXaXFxQURpST02NkRDOTU3Qw==")
                .pathParam("issueId", issueId)
                .log().all()
                .delete("/rest/api/3/issue/{issueId}")
                .then().log().all()
                .assertThat().statusCode(204)
                .extract().response().asString();
    }
}
