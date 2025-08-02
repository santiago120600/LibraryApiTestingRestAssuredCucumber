package io.github.santiago120600.runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/java/io/github/santiago120600/features",
    plugin = {
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
        "pretty",
        "summary",
    },
    glue = {"io.github.santiago120600.stepDefinitions"},
    tags = "@AuthorRegression"
)
public class AuthorTestRunner {
}
