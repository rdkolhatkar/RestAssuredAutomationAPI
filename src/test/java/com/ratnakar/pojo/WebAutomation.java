package com.ratnakar.pojo;

/**
 * Represents an individual course under the "Web Automation" category.
 * Each object of this class corresponds to a single course in the JSON list.
 */
public class WebAutomation {

    // Title or name of the web automation course
    private String courseTitle;

    // Price of the course (stored as String for simplicity)
    private String price;

    // Getters and Setters
    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
