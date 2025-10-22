package com.ratnakar.test;

import com.ratnakar.pojo.EcommerceApiLoginTokenRequest;
import com.ratnakar.pojo.EcommerceApiLoginTokenResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class EcommerceApiTest {

    @Test
    public void ecommerceApiEndToEndTest(){
       // Creating request specification which is common for all Ecommerce APIs
       RequestSpecification ecomApiBaseRequestSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/api/ecom")
                .setContentType(ContentType.JSON)
                .build();
       // Creating the object of the EcommerceApiLoginTokenRequest POJO class for setting the values
        EcommerceApiLoginTokenRequest ecommerceApiLoginTokenRequest = new EcommerceApiLoginTokenRequest();
        ecommerceApiLoginTokenRequest.setUserEmail("ratnakarkolhatkar@gmail.com");
        ecommerceApiLoginTokenRequest.setUserPassword("Ratanlord@1409");
       // Generating and Retrieving the HTTP Authorization token
       RequestSpecification loginRequest = given().spec(ecomApiBaseRequestSpec).log().all().body(ecommerceApiLoginTokenRequest);
       // Calling the actual POST API call for Generating the Auth token using "loginRequest"
       EcommerceApiLoginTokenResponse loginResponse = loginRequest.when().post("/auth/login")
               .then().log().all()
               .extract().response().as(EcommerceApiLoginTokenResponse.class);
       System.out.println("Authorization Token Value is : "+loginResponse.getToken());
        System.out.println("UserID value is : "+loginResponse.getUserId());

    }
}
