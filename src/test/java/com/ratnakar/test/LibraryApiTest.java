package com.ratnakar.test;

import com.ratnakar.data.AddPlaceApiPayLoad;
import com.ratnakar.utils.JsonPathMethod;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class LibraryApiTest {
    @Test
    public void dynamicJsonTest(){
        // Add Book API
        RestAssured.baseURI = "http://216.10.245.166";
        String libraryApiResponse = given().log().all().header("Content-Type", "application/json")
                .body(AddPlaceApiPayLoad.addPlacePayLoad())
                .when().post("/Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();
        System.out.println(libraryApiResponse);
        JsonPath jsonPath = JsonPathMethod.rawDataToJsonConverter(libraryApiResponse);
        String id = jsonPath.getString("ID");
        System.out.println(id);
    }
}
