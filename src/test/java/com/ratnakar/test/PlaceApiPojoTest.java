package com.ratnakar.test;

import com.ratnakar.pojo.AddPlaceApiLocation;
import com.ratnakar.pojo.AddPlaceApiRequest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class PlaceApiPojoTest {
    @Test
    public void addPlaceApiStaticJsonTest() throws IOException {

        // Creating Object of AddPlaceApiRequest POJO class for serialization

        AddPlaceApiRequest addPlaceApiRequest = new AddPlaceApiRequest();
        addPlaceApiRequest.setAccuracy(50);
        addPlaceApiRequest.setAddress("29, side layout, cohen 09");
        addPlaceApiRequest.setLanguage("French-IN");
        addPlaceApiRequest.setPhone_number("(+91) 983 893 3937");
        addPlaceApiRequest.setWebsite("https://rahulshettyacademy.com");
        addPlaceApiRequest.setName("Frontline House");
        List<String> typesList = new ArrayList<String>();
        typesList.add("shoe park");
        typesList.add("shop");
        addPlaceApiRequest.setTypes(typesList);
        AddPlaceApiLocation location = new AddPlaceApiLocation();
        location.setLat(-38.383494);
        location.setLng(33.427362);
        addPlaceApiRequest.setLocation(location);

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(addPlaceApiRequest) // Passing the POJO class called AddPlaceApiRequest
                .when().post("/maps/api/place/add/json")
                .then().log().all()
                .assertThat().statusCode(200)
                .body("scope", equalTo("APP"))
                .extract().response().asString();

        System.out.println(response);

        // Extract place_id from response
        JsonPath jsonPath = new JsonPath(response);
        String placeID = jsonPath.getString("place_id");
        System.out.println("The place_id is :"+placeID);
    }


}
