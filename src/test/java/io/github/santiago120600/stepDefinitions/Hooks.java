package io.github.santiago120600.stepDefinitions;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.github.santiago120600.testutils.Utils;
import io.github.santiago120600.testutils.ExtentRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;

public class Hooks {

    public static RequestSpecification spec;
    private static String baseUrl;
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

    @BeforeAll
    public static void setEnv() throws IOException {
        String env = System.getProperty("env", "mock");
        logger.info("Environment configuration - env: {}", env);

        logger.debug("Runner file selected: {}",System.getProperty("test"));
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
        logger.info("Base URL set to: {}", baseUrl);
    }

    @Before
    public void setup() throws IOException {
        RestAssured.defaultParser = Parser.JSON;
        if (System.getProperty("config").equals("local")) {
            RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        }
        String proxy = System.getProperty("proxy", "true");
        logger.info("Proxy configuration - proxy: {}", proxy);
        if (Boolean.parseBoolean(proxy)) {
            RestAssured.proxy(Utils.getGlobalValue("proxy.base.url"));
        }
        spec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .addFilter(new ExtentRestAssured())
                .build();
    }
}
