package com.ratnakar.test;

import com.ratnakar.data.AddPlaceApiPayLoad;
import com.ratnakar.pojo.AddPlaceApiLocation;
import com.ratnakar.pojo.AddPlaceApiRequest;
import com.ratnakar.utils.JsonPathMethod;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class RequestResponseSpecBuilderTest {

    // Request Spec Builder
    RequestSpecification requestSpecBuilder =  new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
            .addQueryParam("key", "qaclick123")
            .setContentType(ContentType.JSON)
            .build();
    // Response Spec Builder
    ResponseSpecification responseSpecification = new ResponseSpecBuilder().expectStatusCode(200)
            .expectContentType(ContentType.JSON)
            .build();

    // Defining variable PlaceID
    String placeID;

    @Test
    public void addPlaceApiTestAssertions(){
        // Pojo for place API
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
        // Request for add place api with requestSpecifications
        RequestSpecification addPlaceApiRequestSpecifications = given().log().all().spec(requestSpecBuilder) // calling requestSpecBuilder
                .body(addPlaceApiRequest);
        // Response for add place api with requestSpecifications
        String placeApiResponse = addPlaceApiRequestSpecifications.when().post("/maps/api/place/add/json")
                .then().log().all().spec(responseSpecification) // calling responseSpecification
                .extract().asString();
        System.out.println(placeApiResponse);
        JsonPath jsonPath = new JsonPath(String.valueOf(placeApiResponse));
        placeID = jsonPath.getString("place_id");
    }
}
