package com.ratnakar.cucumber.stepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

public class StepDefinitions {
    @Given("the base URI is {string}")
    public void the_base_uri_is(String string) {
    }
    @And("the payload is read from {string}")
    public void the_payload_is_read_from(String string) {
    }
    @When("I replace the placeholders in the payload with:")
    public void i_replace_the_placeholders_in_the_payload_with(DataTable dataTable) {
    }
    @And("I send a POST request to {string}")
    public void i_send_a_post_request_to(String string) {
    }
    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer int1) {
    }
    @And("the response body should contain {string} as {string}")
    public void the_response_body_should_contain_as(String string, String string2) {
    }
}
