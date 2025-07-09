package io.github.santiago120600.stepDefinitions;

import java.io.IOException;

import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.github.santiago120600.resources.Utils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Hooks {

    public static RequestSpecification spec;
    private static String baseUrl;

    @BeforeAll
    public static void setEnv() throws IOException {
        String env = System.getProperty("env", "mock");
        switch (env) {
            case "mock":
                baseUrl = Utils.getGlobalValue("mock.base.url");
                break;
            case "prod":
                baseUrl = Utils.getGlobalValue("prod.base.url");
                break;
            default:
                baseUrl = Utils.getGlobalValue("mock.base.url");
        }
    }

    @Before
    public void setup() throws IOException {
        RestAssured.proxy(Utils.getGlobalValue("proxy.base.url"));
        spec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .build();
    }
}
