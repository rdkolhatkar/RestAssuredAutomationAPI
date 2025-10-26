package com.ratnakar.test;

import com.ratnakar.data.LibraryApiPayload;
import com.ratnakar.utils.JsonPathMethod;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class LibraryApiTest {
    @Test
    public void dynamicJsonTest(){
        // Add Book API
        RestAssured.baseURI = "http://216.10.245.166";
        String libraryApiResponse = given().log().all().header("Content-Type", "application/json")
                .body(LibraryApiPayload.addBookPayload())
                .when().post("/Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();
        System.out.println(libraryApiResponse);
        JsonPath jsonPath = JsonPathMethod.rawDataToJsonConverter(libraryApiResponse);
        String id = jsonPath.getString("ID");
        System.out.println(id);
    }
    @Test
    public void parameterizedPayloadTest(){
        RestAssured.baseURI = "http://216.10.245.166";
        String libraryApiResponse = given().log().all().header("Content-Type", "application/json")
                .body(LibraryApiPayload.AddBookPayload("887", "dfsgh")) // Passing dynamic values for "aisle" & "isbn"
                .when().post("/Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();
        System.out.println(libraryApiResponse);
    }

    /*
    In TestNG, @DataProvider is used to supply multiple sets of data to a single test method, enabling data-driven testing.
    It returns an Object[][] where each row is a set of parameters. The test method is executed once per data set.
    A DataProvider method must be annotated with @DataProvider and referenced in the test using @Test(dataProvider="name").
    It helps avoid code duplication, supports parallel execution, and makes tests flexible and reusable.
    */
    @DataProvider(name="LibraryApiTestData")
    public Object[][] getData(){
        // Array is collection of elements
        // Multidimensional array is collection of arrays
        return new Object[][]{{"ojfws","9987"}, {"csddf","2244"}, {"jksm","5034"}};
    }
    // Implementing above DataProvider
    // Below test will run for 3 times as we have 3 arrays in DataProvider
    // With this annotation it will dynamically fetch the isbn and aisel from getData() method array object
    @Test(dataProvider="LibraryApiTestData") // This will only work if @Test is imported from TestNG
    public void dynamicDataProviderPayloadTest(String isbn, String aisel){
        // For every test execution only one Array element will be returned by data provider and test will run again and again for remaining arrays
        RestAssured.baseURI = "http://216.10.245.166";
        String libraryApiResponse = given().log().all().header("Content-Type", "application/json")
                .body(LibraryApiPayload.AddBookPayload(isbn,aisel)) // Passing dynamic values for "aisle" & "isbn"
                .when().post("/Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();
        System.out.println(libraryApiResponse);
    }
    // Delete API
    @DataProvider(name="LibraryApiDeleteData")
    public Object[][] getDeleteData(){
        return new Object[][]{
                {"ojfws","9987"},
                {"csddf","2244"},
                {"jksm","5034"}
        };
    }
    @Test(dataProvider="LibraryApiDeleteData")
    public void dynamicDeleteApiTest(String isbn, String aisel){
        RestAssured.baseURI = "http://216.10.245.166";
        String id = isbn + aisel; // concatenate isbn and aisel

        String deleteResponse = given().log().all()
                .header("Content-Type", "application/json")
                .body(LibraryApiPayload.DeleteBookPayload(id)) // using payload method
                .when().post("/Library/DeleteBook.php")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response().asString();

        System.out.println("Deleted Book ID: " + id);
        System.out.println("Response: " + deleteResponse);
    }

    // Passing data from Static Json file into the API test
    // There is method in java which can read the content of our file and convert it into byte
    // Once we get the byte data then we have to convert the byte data to String
    @Test
    public void staticJsonPayloadTest() throws IOException {
        RestAssured.baseURI = "http://216.10.245.166";
        String libraryApiResponse = given().log().all().header("Content-Type", "application/json")
                /*
                This code reads a JSON file from src/test/resources/JsonData/AddBook.json, converts its contents into a string, and passes it as the request body.
                Files.readAllBytes(Paths.get(...)) loads file bytes, new String(...) converts them to text, and .body(...) sends that JSON string as payload in the API request.
                */
                .body(new String((Files.readAllBytes(Paths.get("src/test/resources/JsonData/AddBook.json")))))
                .when().post("/Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();
        System.out.println(libraryApiResponse);
    }

}
