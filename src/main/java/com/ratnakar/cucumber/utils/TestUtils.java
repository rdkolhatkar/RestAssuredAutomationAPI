package com.ratnakar.cucumber.utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.io.*;
import java.util.Properties;

public class TestUtils {

    public static RequestSpecification requestSpecBuilder;
    public RequestSpecification requestSpecification() throws IOException {
        if(requestSpecBuilder == null){
            PrintStream logPrintStream = new PrintStream(new FileOutputStream("logging.txt"));

            requestSpecBuilder =  new RequestSpecBuilder().setBaseUri(getGlobalConfigurationProperties("appBaseUri"))
                    .addQueryParam("key", "qaclick123")
                    .addFilter(RequestLoggingFilter.logRequestTo(logPrintStream)) // adding filter for logging the API request
                    .addFilter(ResponseLoggingFilter.logResponseTo(logPrintStream)) // adding filter for logging the API response
                    .setContentType(ContentType.JSON)
                    .build();

            return requestSpecBuilder;
        }
        return requestSpecBuilder;
    }

    public static String getGlobalConfigurationProperties(String key) throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/global.properties");
        properties.load(fileInputStream);
        return properties.getProperty(key);
    }
}
