package com.ratnakar.data;

public class LibraryApiPayload {
    public static String addBookPayload(){
        String addBookRequestBody = "{\n" +
                "  \"name\": \"Learn Appium Automation with Java\",\n" +
                "  \"isbn\": \"bcd\",\n" +
                "  \"aisle\": \"227\",\n" +
                "  \"author\": \"John foe\"\n" +
                "}\n";
        return addBookRequestBody;
    }
}
