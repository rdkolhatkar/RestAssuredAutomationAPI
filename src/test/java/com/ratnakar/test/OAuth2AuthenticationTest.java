package com.ratnakar.test;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class OAuth2AuthenticationTest {

    public void getAccessToken(){
        given()
                .queryParams("code", "")
                .queryParams("client_id", "")
                .queryParams("client_secret", "")
                .queryParams("redirect_uri", "")
                .queryParams("grant_type", "");
    }

    @Test
    public void oAuth2AuthenticationTest(){
        String response = given().log().all()
                .queryParam("access_token", "")
                .when()
                .get("https://rahulshettyacademy.com/getCourse.php")
                .then().extract().response().asString();

        System.out.println(response);
    }
}
