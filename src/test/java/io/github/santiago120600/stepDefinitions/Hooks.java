package io.github.santiago120600.stepDefinitions;

import java.io.IOException;

import io.cucumber.java.Before;
import io.github.santiago120600.resources.Utils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Hooks {

    public static RequestSpecification spec;

    @Before
    public void setup() throws IOException {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        spec = new RequestSpecBuilder()
                .setBaseUri(Utils.getGlobalValue("baseUrl"))
                .setContentType(ContentType.JSON)
                .build();
    }
}
