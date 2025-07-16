package io.github.santiago120600.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/java/io/github/santiago120600/features",
    plugin = {
        "pretty",
        "summary",
        "html:target/report/cucumber-reports.html",
        "json:target/report/cucumber.json",
        "junit:target/report/cucumber.xml"
    },
    glue = {"io.github.santiago120600.stepDefinitions"},
    tags = "@BookRegression"
)
public class BookTestRunner {
}
