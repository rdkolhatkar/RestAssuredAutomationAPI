package com.ratnakar.test;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class OAuth2AuthenticationTest {

    // Pass the 'oneTimePassCode' value from the program arguments or VM Options. "-DoneTimePassCode='4%2F0Aci98E_In9-uM3FtM8tPy5rjvflX6fFQZKEHhffqgzDgrBWDbQtFfY9e3CxDBsxzO-jXoA'"
    public static String getAccessToken(){
        String accessTokenResponse = given().log().all()
                .queryParams("code", System.getProperty("oneTimePassCode"))
                .queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                .queryParams("grant_type", "authorization_code")
                .when().log().all()
                .post("https://www.googleapis.com/oauth2/v4/token").asString();
        JsonPath jsonPath = new JsonPath(accessTokenResponse);
        String accessToken = jsonPath.getString("access_token");
        return accessToken;

    }

    // urlEncodingEnabled(false) tells REST Assured not to automatically encode special characters (like %, :, /, +) in the URL or query parameters, so the exact value is sent as-is.
    @Test
    public void oAuth2AuthenticationTest(){
        String response = given().log().all().urlEncodingEnabled(false)
                .queryParam("access_token", "")
                .when()
                .get("https://rahulshettyacademy.com/getCourse.php")
                .then().extract().response().asString();

        System.out.println(response);
    }
}
