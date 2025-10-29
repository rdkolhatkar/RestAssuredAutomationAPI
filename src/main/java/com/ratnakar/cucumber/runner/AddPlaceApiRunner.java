package com.ratnakar.cucumber.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/main/resources/features",
        glue = "com.ratnakar.cucumber.stepDefinitions",
        tags = "@PlaceApi",
        monochrome = true,
        plugin = {"pretty", "html:target/cucumber-reports/reports.html", "json:target/cucumber-reports/cucumber.json"}
)
public class AddPlaceApiRunner extends AbstractTestNGCucumberTests {
}
