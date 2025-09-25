package com.ratnakar.data;

public class LibraryApiPayload {
    public static String addBookPayload(){
        String addBookRequestBody = "{\n" +
                "  \"name\": \"Learn Appium Automation with Java\",\n" +
                "  \"isbn\": \"bcjd\",\n" +
                "  \"aisle\": \"227\",\n" +
                "  \"author\": \"John foe\"\n" +
                "}\n";
        return addBookRequestBody;
    }

    public static String AddBookPayload(String aisle, String isbn){
        String addBookRequestBody = "{\n" +
                "  \"name\": \"Learn Appium Automation with Java\",\n" +
                "  \"isbn\": \""+isbn+"\",\n" +
                "  \"aisle\": \""+aisle+"\",\n" +
                "  \"author\": \"John foe\"\n" +
                "}\n";
        return addBookRequestBody;
    }

    public static String DeleteBookPayload(String id) {
        return "{\n" +
                "  \"ID\": \"" + id + "\"\n" +
                "}";
    }
}
