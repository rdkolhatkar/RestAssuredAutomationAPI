package com.ratnakar.pojo;

/**
 * The main POJO (Plain Old Java Object) representing a user's course details.
 * This class will be used as the root object when serializing or deserializing
 * JSON responses in RestAssured automation.
 *
 * Each field here maps to a key in the JSON response.
 */
public class GetCourses {
    // URL of the website or course provider
    private String url;

    // Type of services offered (e.g., Education, Automation Training)
    private String services;

    // Area of expertise (e.g., Testing, Automation, DevOps)
    private String expertise;

    // Contains lists of all course categories like web, API, and mobile
    private CourseList courses;

    // Name of the instructor or course provider
    private String instructor;

    // Instructor's LinkedIn profile link
    private String linkedIn;

    // Getters and Setters: used by RestAssured and Jackson to access JSON data
    public CourseList getCourses() {
        return courses;
    }

    public void setCourses(CourseList courses) {
        this.courses = courses;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }
}
