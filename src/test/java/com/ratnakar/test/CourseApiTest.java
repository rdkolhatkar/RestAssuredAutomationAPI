package com.ratnakar.test;

import com.ratnakar.data.CourseApiPayLoad;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class CourseApiTest {

    @Test
    public void mockApiServiceTest(){
        // Below we have mocked the Json response of the course API using JsonPath
        JsonPath jsonPath = new JsonPath(CourseApiPayLoad.courseApiResponsePayload());
    }
}
