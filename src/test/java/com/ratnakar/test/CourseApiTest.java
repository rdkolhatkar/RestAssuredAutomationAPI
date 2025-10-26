package com.ratnakar.test;

import com.ratnakar.data.CourseApiPayLoad;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;


public class CourseApiTest {

    @Test
    public void complexJsonOperationsTest(){
        // Below we have mocked the Json response of the course API using JsonPath
        JsonPath jsonPath = new JsonPath(CourseApiPayLoad.courseApiResponsePayload());
        int courseCount = jsonPath.getInt("courses.size()"); // This will give the count of the courses available in the complex json
        // As "courses" is an array we need to use size() method to get the count of the array elements, hence we use getInt() method
        System.out.println("Count of All Courses Present in the Json is : "+courseCount);
        // Print purchase amount from the complex json
        int totalAmount = jsonPath.getInt("dashboard.purchaseAmount");
        System.out.println("Total Purchase Amount is : "+totalAmount);
        // Print the title of the first course
        // Now we have to write the json path of the "title" where title value is Selenium Python
        String titleOfFirstCourse = jsonPath.get("courses[0].title");
        System.out.println("Title of the First Course is : "+titleOfFirstCourse);
        // As title value is Selenium Python is present on the 0th index of courses Array we are using "courses[0].title"
        // Print all course titles and their respective prices
        for(int i =0; i < courseCount; i++){
            String courseTitle = jsonPath.get("courses[" + i + "].title");
            int coursePrice = jsonPath.get("courses[" + i + "].price");
            System.out.println("Course Title is : "+courseTitle+ " "+"And Course Price is : "+coursePrice
            +" And Number of Copies are : "+ jsonPath.get("courses[" + i + "].copies").toString());
        }
        // Now We have to fetch the copies count for "cypress" course irrespective of the index count. Index count in array may change
        // Now we have to scan each and every element of the array and validate which course title is "cypress" and fetch the "copies" for that title
        System.out.println("Number of Copies sold by Cypress");
        for(int i =0; i < courseCount; i++){
            String courseTitle = jsonPath.get("courses[" + i + "].title");
            if(courseTitle.equalsIgnoreCase("cypress")){
               // Retrieve copies sold for that course
                int copyCountForCypress = jsonPath.get("courses[" + i + "].copies");
                System.out.println(copyCountForCypress);
                break;
            }
        }
        // Now verify if sum of all Course prices matches with purchase amount
        // Total purchaseAmount = {Amount for ("courses[0].title") * Copies Sold for ("courses[0].title")} + {Amount for ("courses[1].title") * Copies Sold for ("courses[1].title")} + .............. + {Amount for ("courses[n].title") * Copies Sold for ("courses[n].title")}
        System.out.println("Validate the Total Amount Displayed");
        int totalPurchaseAmountOfAllCourses = 0;
        for(int i =0; i < courseCount; i++){
            String courseName = jsonPath.get("courses[" + i + "].title");
            int price = jsonPath.getInt("courses[" + i + "].price");
            int copies = jsonPath.getInt("courses[" + i + "].copies");
            int totalSellingCourseAmount = price * copies;
            System.out.println("Total Selling Amount of Course "+courseName +" is "+totalSellingCourseAmount);
            totalPurchaseAmountOfAllCourses = totalPurchaseAmountOfAllCourses + totalSellingCourseAmount;
        }
        System.out.println("Total Purchase Amount of All Courses is : "+totalPurchaseAmountOfAllCourses);
        int purchaseAmount = jsonPath.get("dashboard.purchaseAmount");
        Assert.assertEquals(totalPurchaseAmountOfAllCourses, purchaseAmount);
    }
}
