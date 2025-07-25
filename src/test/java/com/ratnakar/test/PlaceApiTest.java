package com.ratnakar.test;

import com.ratnakar.data.AddPlaceApiPayLoad;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

public class PlaceApiTest {

    String placeID;
    @Test
    public void addPlaceApiTest(){
        // Validate if Add Place Api is Working as Expected.
        // given() -> Using this method we build the API request with all input data & parameters including respective headers
        // when() -> We use this method to submit the API resource for HTTP methods like Get, Post, Delete etc.
        // then() -> We use this method for Validation of API response by providing the Assertions

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        given().log().all()
                .queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"location\": {\"lat\": -38.383494, \"lng\": 33.427362},\n" +
                        "  \"accuracy\": 50,\n" +
                        "  \"name\": \"Frontline house\",\n" +
                        "  \"phone_number\": \"(+91) 983 893 3937\",\n" +
                        "  \"address\": \"29, side layout, cohen 09\",\n" +
                        "  \"types\": [\"shoe park\", \"shop\"],\n" +
                        "  \"website\": \"http://google.com\",\n" +
                        "  \"language\": \"French-IN\"\n" +
                        "}\n")
                .when().post("/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200);
    }
    @Test
    public void addPlaceApiTestAssertions(){
        // Validate if Add Place Api is Working as Expected.
        // equalTo(object) -> This static method is implemented from package org.hamcrest.CoreMatchers for comparing the response objects
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all()
                .queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(AddPlaceApiPayLoad.addPlacePayLoad())
                .when().post("/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("Server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();
        // Extracting response as a String value in the response object to extract the placeID
        System.out.println(response);
        JsonPath jsonPath = new JsonPath(response);
        placeID = jsonPath.getString("place_id");
    }

    // Add place & fetch the placeID from api response. And for the same placeID update the details using update place api & Check if place is updated or not using get place api
    @Test
    public void updatePlaceApiTest(){
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"place_id\": \""+placeID+"\",\n" +
                        "  \"address\": \"70 Summer walk, USA\",\n" +
                        "  \"key\": \"qaclick123\"\n" +
                        "}")
                .when().put("/maps/api/place/update/json")
                .then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
    }
    // Now we will be calling the GET Place API to check if our address is successfully updated or not
}

