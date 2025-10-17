package com.ratnakar.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ratnakar.pojo.ApiAutomation;
import com.ratnakar.pojo.GetCourses;
import com.ratnakar.pojo.WebAutomation;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class CourseApiPojoTest {
    @Test
    public void pojoDeserializationTest() throws JsonProcessingException {
        // Get HTTP Auth Token as "access_token"
        String responseToken = 	given()
                .formParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
                .formParams("grant_type","client_credentials")
                .formParams("scope","trust")
                .when().log().all()
                .post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token").asString();
        System.out.println(responseToken);
        JsonPath jsonPath = new JsonPath(responseToken);
        String accessToken = jsonPath.getString("access_token");

        // Invoke Get Courses API With Access Token
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        // Extracting Response from Course API & Storing it inside POJO class called "GetCourses"
        GetCourses getCourseResponse = given()
                .queryParam("access_token", accessToken)
                .when().log().all()
                .get("/oauthapi/getCourseDetails").as(GetCourses.class);
        // Printing API response Json into console
        // converting POJO class back to a readable JSON string using Jackson’s ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // for pretty print
        String jsonString = mapper.writeValueAsString(getCourseResponse);
        System.out.println("Response JSON:\n" + jsonString);
        // Extracting response fields from GetCourse
        String linkedInAccount = getCourseResponse.getLinkedIn();
        String instructorName =  getCourseResponse.getInstructor();
        System.out.println("LinkedIn Url is : "+linkedInAccount);
        System.out.println("Instructor Name is : "+instructorName);
        // Now we have to extract the price of a specific course
        // And we have to add the price of all the courses in the api response and verify the total amount
        // Extract courses price and get course title under API Course
        List<ApiAutomation> apiCourses = getCourseResponse.getCourses().getApi();
        for(int i = 0; i < apiCourses.size(); i++){
            String courseName = "SoapUI Webservices testing";
            if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase(courseName)){
                String price = apiCourses.get(i).getPrice();
                System.out.println("Price of the "+courseName+" is : "+price);
            }
        }
        // Extract courses price and get course title under Web Automation Course
        List<WebAutomation> webAutomationCourses = getCourseResponse.getCourses().getWebAutomation();
        String[] webAutomationCourseTitles = {"Selenium Webdriver Java","Cypress","Protractor"};
        for(int j = 0; j < webAutomationCourses.size(); j++){
            String courseTitle = Arrays.stream(webAutomationCourseTitles).toList().get(j).toString();
            if(webAutomationCourses.get(j).getCourseTitle().equalsIgnoreCase(courseTitle)){
                String price = webAutomationCourses.get(j).getPrice();
                System.out.println("Price of the "+courseTitle+" is : "+price);
            }
        }
        // Extract the course Titles for Web Automation from the Course API response
        String[] webAutomationCourseList = {"Selenium Webdriver Java","Cypress","Protractor"};
        ArrayList<String> arrayList =  new ArrayList<String>();
        for(int k = 0; k < webAutomationCourses.size(); k++){
           arrayList.add(webAutomationCourses.get(k).getCourseTitle());
        }
        List<String> courseArrayList = Arrays.asList(webAutomationCourseList);
        Assert.assertTrue(arrayList.equals(courseArrayList));
    }
}
