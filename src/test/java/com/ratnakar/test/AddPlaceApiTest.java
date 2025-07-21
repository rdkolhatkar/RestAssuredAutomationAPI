package com.ratnakar.test;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

public class AddPlaceApiTest {
    @Test
    public void addPlaceApiTest(){
        // Validate if Add Place Api is Working as Expected.
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
}

