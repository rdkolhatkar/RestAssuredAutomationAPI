package com.ratnakar.pojo;

import java.util.List;

/**
 * This class groups different types of automation courses.
 * Each list represents a category of courses (Web, API, or Mobile).
 *
 * For example, in JSON:
 * "courses": {
 *   "webAutomation": [...],
 *   "api": [...],
 *   "mobile": [...]
 * }
 */
public class CourseList {

    // List of all web automation courses
    private List<WebAutomation> webAutomation;

    // List of all API automation courses
    private List<ApiAutomation> api;

    // List of all mobile automation courses
    private List<MobileAutomation> mobile;

    // Getters and Setters
    public List<WebAutomation> getWebAutomation() {
        return webAutomation;
    }

    public void setWebAutomation(List<WebAutomation> webAutomation) {
        this.webAutomation = webAutomation;
    }

    public List<ApiAutomation> getApi() {
        return api;
    }

    public void setApi(List<ApiAutomation> api) {
        this.api = api;
    }

    public List<MobileAutomation> getMobile() {
        return mobile;
    }

    public void setMobile(List<MobileAutomation> mobile) {
        this.mobile = mobile;
    }
}
