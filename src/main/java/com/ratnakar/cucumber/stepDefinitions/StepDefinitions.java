package com.ratnakar.cucumber.stepDefinitions;


import com.ratnakar.cucumber.model.AddPlaceApiLocation;
import com.ratnakar.cucumber.model.AddPlaceApiRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class StepDefinitions {

    private AddPlaceApiRequest addPlaceApiRequest;
    private Response response;

    private String addPlaceApiPayload;
    private String baseUri;
    private Response addPlaceResponse;

    @Given("the base URI is {string}")
    public void the_base_uri_is(String baseUri) {
        RestAssured.baseURI = baseUri;
    }

    @And("the payload is derived from POJO classes")
    public void the_payload_is_derived_from_pojo_classes() {
        addPlaceApiRequest = new AddPlaceApiRequest();

        // Default static payload setup
        addPlaceApiRequest.setAccuracy(50);
        addPlaceApiRequest.setAddress("29, side layout, cohen 09");
        addPlaceApiRequest.setLanguage("French-IN");
        addPlaceApiRequest.setPhone_number("(+91) 983 893 3937");
        addPlaceApiRequest.setWebsite("https://rahulshettyacademy.com");
        addPlaceApiRequest.setName("Frontline House");

        List<String> typesList = new ArrayList<>();
        typesList.add("shoe park");
        typesList.add("shop");
        addPlaceApiRequest.setTypes(typesList);

        AddPlaceApiLocation location = new AddPlaceApiLocation();
        location.setLat(-38.383494);
        location.setLng(33.427362);
        addPlaceApiRequest.setLocation(location);
    }

    @When("I replace the placeholders in the payload with:")
    public void i_replace_the_placeholders_in_the_payload_with(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        // Dynamically set values if present
        if (data.containsKey("lat")) {
            addPlaceApiRequest.getLocation().setLat(Double.parseDouble(data.get("lat")));
        }
        if (data.containsKey("lng")) {
            addPlaceApiRequest.getLocation().setLng(Double.parseDouble(data.get("lng")));
        }
        if (data.containsKey("accuracy")) {
            addPlaceApiRequest.setAccuracy(Integer.parseInt(data.get("accuracy")));
        }
        if (data.containsKey("name")) {
            addPlaceApiRequest.setName(data.get("name"));
        }

        System.out.println("\nFinal Request Body:");
        System.out.println(io.restassured.path.json.JsonPath.from(io.restassured.path.json.JsonPath.given(String.valueOf(addPlaceApiRequest)).toString()));
    }

    @And("I send a POST request to {string}")
    public void i_send_a_post_request_to(String endpoint) {
        response = given()
                .log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(addPlaceApiRequest)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract()
                .response();

        System.out.println("Response Body:\n" + response.asString());
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatus) {
        assertThat(response.statusCode()).isEqualTo(expectedStatus);
    }

    @And("the response body should contain {string} as {string}")
    public void the_response_body_should_contain_as(String key, String expectedValue) {
        String responseBody = response.getBody().asString();

        if (responseBody == null || responseBody.isEmpty()) {
            throw new AssertionError("Response body is empty. Cannot validate key: " + key);
        }

        JsonPath jsonPath = new JsonPath(responseBody);
        String actualValue = jsonPath.getString(key);
        assertThat(actualValue).isEqualTo(expectedValue);
    }
    @Given("the base URI of Place Web Service is {string}")
    public void theBaseURIOfPlaceWebServiceIs(String uri) {
        this.baseUri = uri;
    }
    @And("I read the payload template from {string}")
    public void iReadThePayloadTemplateFrom(String filePath) throws IOException {
        addPlaceApiPayload = new String(Files.readAllBytes(Paths.get(filePath)));
    }
    @When("I replace placeholders in the payload with:")
    public void iReplacePlaceholdersInThePayloadWith(DataTable dataTable) {
        Map<String, String> replacements = dataTable.asMap();
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            addPlaceApiPayload = addPlaceApiPayload.replace("<" + entry.getKey() + ">", entry.getValue());
        }
    }
    @And("I send a POST API request to {string}")
    public void iSendAPOSTAPIRequestTo(String endpoint) {
        RequestSpecification request = RestAssured.given()
                .baseUri(baseUri)
                .header("Content-Type", "application/json")
                .body(addPlaceApiPayload);
        response = request.post(endpoint);
    }
}
