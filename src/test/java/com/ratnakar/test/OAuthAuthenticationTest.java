package com.ratnakar.test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;

public class OAuthAuthenticationTest {

    @Test
    public void OAuthGetAccessTokenRestAssuredTest(){
        // Calling the Auth Server to get the Access Token
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String authResponse = given().log().all()
                .formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParam("client_secret","erZOWM9g3UtwNRj340YYaK_W")
                .formParam("grant_type", "client_credentials")
                .formParam("scope", "trust")
                .when().log().all()
                .post("/oauthapi/oauth2/resourceOwner/token")
                .asString();
        System.out.println(authResponse);
        JsonPath jsonPath = new JsonPath(authResponse);
        String accessToken = jsonPath.getString("access_token");

        // Using the above "accessToken" value we can call our actual API service
        String coursesApiResponse = given().queryParam("access_token", accessToken)
                .when().log().all()
                .get("https://rahulshettyacademy.com/oauthapi/getCourseDetails")
                .asString();
        System.out.println(coursesApiResponse);

    }
}
