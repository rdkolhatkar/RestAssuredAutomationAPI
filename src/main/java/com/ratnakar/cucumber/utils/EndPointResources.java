package com.ratnakar.cucumber.utils;

// An enum class in Java is a special class that defines a fixed set of named, type-safe constants.
public enum EndPointResources {
    AddPlaceApi("/maps/api/place/add/json"),
    GetPlaceApi("/maps/api/place/get/json"),
    DeletePlaceApi("/maps/api/place/delete/json");
    private String resource;
    EndPointResources(String resource) {
        this.resource = resource;
    }
    public String getResource(){
        return resource;
    }
}
