package com.atom.healthcare.TestRunner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "./src/main/java/com/atom/healthcare/Features/HealthCare.feature", glue = {
		"com.atom.healthcare.stepDefinations", "com.atom.healthcare.Hooks" }, plugin = {
				"pretty", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
				"json:target/cucumber-reports/cucumber.json", "html:target/cucumber-reports/cucumber.html",
				"junit:target/cucumber-reports/cucumber.xml", "pretty:target/cucumber-reports/cucumber-pretty.txt",
				"usage:target/cucumber-reports/cucumber-usage.json",
				"rerun:target/cucumber-reports/rerun.txt" }, tags = "@Regression", dryRun = false, monochrome = true)
public class TestRunner {

}
