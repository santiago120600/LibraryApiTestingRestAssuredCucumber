package io.github.santiago120600.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/java/io/github/santiago120600/features",
    plugin = {
        "pretty",
    },
    glue = {"io.github.santiago120600.stepDefinitions"},
    tags = "@AddBook"
)
public class BookTestRunner {
}
