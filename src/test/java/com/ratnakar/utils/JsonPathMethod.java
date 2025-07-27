package com.ratnakar.utils;

import io.restassured.path.json.JsonPath;

public class JsonPathMethod {
    public static JsonPath rawDataToJsonConverter(String response){
        JsonPath jsonPath = new JsonPath(response);
        return jsonPath;
    }
}
