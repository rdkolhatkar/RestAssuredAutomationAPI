@AddPlaceApi
Feature: Validate Add Place API

  Scenario Outline: Validate Add Place API with dynamic test data
    Given the base URI is "https://rahulshettyacademy.com"
    And the payload is derived from POJO classes
    When I replace the placeholders in the payload with:
      | key        | value       |
      | <latitude> | <longitude> |
      | <accuracy> | <name>      |
    And I send a POST request to "/maps/api/place/add/json"
    Then the response status code should be 200
    And the response body should contain "scope" as "APP"

    Examples:
      | latitude   | longitude  | accuracy | name            |
      | -38.383494 | 33.427362  | 50       | Frontline house |