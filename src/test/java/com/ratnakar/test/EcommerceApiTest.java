package com.ratnakar.test;

import com.ratnakar.pojo.CreateOrderApiBase;
import com.ratnakar.pojo.CreateOrderApiDetails;
import com.ratnakar.pojo.EcommerceApiLoginTokenRequest;
import com.ratnakar.pojo.EcommerceApiLoginTokenResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class EcommerceApiTest {

    @Test
    public void ecommerceApiEndToEndTest(){
       // Creating request specification which is common for all Ecommerce APIs
       // Bypass the SSL certification
       RequestSpecification ecomApiBaseRequestSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/api/ecom")
                .setContentType(ContentType.JSON)
                .build();
       // Creating the object of the EcommerceApiLoginTokenRequest POJO class for setting the values
        EcommerceApiLoginTokenRequest ecommerceApiLoginTokenRequest = new EcommerceApiLoginTokenRequest();
        ecommerceApiLoginTokenRequest.setUserEmail("ratnakarkolhatkar@gmail.com");
        ecommerceApiLoginTokenRequest.setUserPassword("Ratanlord@1409");
       // Generating and Retrieving the HTTP Authorization token
       RequestSpecification loginRequest = given().relaxedHTTPSValidation().spec(ecomApiBaseRequestSpec).log().all().body(ecommerceApiLoginTokenRequest);
       // Calling the actual POST API call for Generating the Auth token using "loginRequest"
       EcommerceApiLoginTokenResponse loginResponse = loginRequest.when().post("/auth/login")
               .then().log().all()
               .extract().response().as(EcommerceApiLoginTokenResponse.class);
       String token = loginResponse.getToken();
       String userId = loginResponse.getUserId();
       System.out.println("Authorization Token Value is : "+loginResponse.getToken());
       System.out.println("UserID value is : "+loginResponse.getUserId());
       // Now we have to Add the product to Ecommerce Application, But here our request input is in the form of "form-data"
       // Creating new request specification for Add Product API
        RequestSpecification addProductApiRequestSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/api/ecom")
                .addHeader("Authorization", token)
                .build();
        RequestSpecification addProductRequest = given().relaxedHTTPSValidation().log().all().spec(addProductApiRequestSpec)
                // We use .param() method for passing the "form-data" as request
                .param("productName", "qwerty")
                .param("productAddedBy", userId)
                .param("productCategory", "fashion")
                .param("productSubCategory", "shirts")
                .param("productPrice", 1150)
                .param("productDescription", "Adidas Originals")
                .param("productFor","women")
                .multiPart("productImage", new File("src/test/resources/ImageFileInput/BrownJacketForWomen.jpg"));
        String addProductResponse = addProductRequest.when().post("/product/add-product")
                .then().log().all()
                .extract().response().asString();
        JsonPath jsonPath = new JsonPath(addProductResponse);
        String productId = jsonPath.get("productId");
        System.out.println("Product Id is : "+productId);
        // Now we will create the order with "CreateOrder API"
        RequestSpecification createOrderRequestSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/api/ecom")
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", token)
                .build();
        // Calling the POJO classes CreateOrderApiDetails & CreateOrderApiBase
        CreateOrderApiDetails createOrderApiDetails = new CreateOrderApiDetails();
        createOrderApiDetails.setCountry("India");
        createOrderApiDetails.setProductOrderedId(productId);
        // Creating the List of CreateOrderApiDetails
        List<CreateOrderApiDetails> orderDetailsPayload = new ArrayList<CreateOrderApiDetails>();
        orderDetailsPayload.add(createOrderApiDetails);
        CreateOrderApiBase createOrderApiBase = new CreateOrderApiBase();
        createOrderApiBase.setOrders(orderDetailsPayload);
        // Creating the request specification for CreateOrder API Request
        RequestSpecification createOrderRequest = given().relaxedHTTPSValidation().log().all().spec(createOrderRequestSpec)
                .body(createOrderApiBase);
        String createOrderResponse = createOrderRequest.when().post("/order/create-order")
                .then().log().all()
                .extract().response().asString();
        System.out.println(createOrderResponse);
        // Now after creating new Order we have to delete the existing product
        // Here to delete the product we have to send the productId as Path Parameter
        RequestSpecification deleteProductRequestSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/api/ecom")
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", token)
                .build();
        RequestSpecification deleteProductRequest = given().relaxedHTTPSValidation().log().all().spec(deleteProductRequestSpec)
                .pathParam("productId",productId);
        String deleteProductResponse = deleteProductRequest.when().delete("/product/delete-product/{productId}")
                .then().log().all().extract().response().asString();
        System.out.println(deleteProductResponse);
        JsonPath deleteProductJson = new JsonPath(deleteProductResponse);
        String deleteProductMessage = deleteProductJson.get("message");
        Assert.assertEquals("Product Deleted Successfully", deleteProductMessage);

    }
}
