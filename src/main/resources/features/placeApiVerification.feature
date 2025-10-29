@PlaceApi
Feature: Validate Add Place API

  Scenario Outline: Validate Add Place API with dynamic test data from JSON file
    Given the base URI of Place Web Service is "https://rahulshettyacademy.com"
    And I read the payload template from "src/main/resources/JsonData/AddPlace.json"
    When I replace placeholders in the payload with:
      | latitude  | <latitude>  |
      | longitude | <longitude> |
      | accuracy  | <accuracy>  |
      | name      | <name>      |
    And I send a POST API request to "/maps/api/place/add/json"
    Then the response status code should be 200
    And the response body should contain "scope" as "APP"

    Examples:
      | latitude   | longitude  | accuracy | name            |
      | -38.383494 | 33.427362  | 50       | Frontline house |
