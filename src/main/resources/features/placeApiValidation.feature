@AddPlaceApi
Feature: Validate Place API

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
      | latitude   | longitude | accuracy | name            |
      | -38.383494 | 33.427362 | 50       | Frontline house |

  Scenario Outline: Validate Get Place API response with dynamic placeId
    Given the base URI of Place Web Service is "https://rahulshettyacademy.com"
    And I have a valid placeId "<placeId>"
    When I invoke the Get Place API
    Then the response status code should be 200
    And the response body should contain "location.lat" as <latitude>
    And the response body should contain "location.lng" as <longitude>
    And response body should contain "accuracy" as <accuracy>
    And the response body should contain "name" as "<name>"
    And the response body should contain "phone_number" as "<phoneNumber>"
    And the response body should contain "address" as "<address>"
    And the response body should contain "types[0]" as "<type1>"
    And the response body should contain "types[1]" as "<type2>"
    And the response body should contain "website" as "<website>"
    And the response body should contain "language" as "<language>"

    Examples:
      | placeId            | latitude   | longitude | accuracy | name            | phoneNumber        | address                   | type1     | type2 | website           | language  |
      | e5Y1g7x0Q1oZxk9QKp | -38.383494 | 33.427362 | 50       | Frontline house | (+91) 983 893 3937 | 29, side layout, cohen 09 | shoe park | shop  | http://google.com | French-IN |

