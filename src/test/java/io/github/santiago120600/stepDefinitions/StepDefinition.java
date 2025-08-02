package io.github.santiago120600.stepDefinitions;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.santiago120600.enums.HttpMethod;
import io.github.santiago120600.models.Author;
import io.github.santiago120600.models.Book;
import io.github.santiago120600.models.ResponseWrapper;
import io.github.santiago120600.testutils.AuthorDataBuilder;
import io.github.santiago120600.testutils.BookDataBuilder;
import io.github.santiago120600.testutils.RequestHelper;
import io.github.santiago120600.testutils.ResponseValidator;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StepDefinition {

    private static final Logger logger = LoggerFactory.getLogger(StepDefinition.class);
    RequestSpecification reqSpec;
    Response response;
    HttpMethod httpMethod;
    Integer statusCode;
    private Author authorPayload;
    private Book bookPayload;

    @Given("I have the following valid {string} data:")
    public void i_have_the_following_valid_data(String entity, DataTable dataTable) throws IOException {
        Map<String, String> data = dataTable.asMaps().get(0);
        switch (entity.toLowerCase()) {
            case "author":
                this.authorPayload = AuthorDataBuilder.addAuthorPayload(data);
                reqSpec = given(Hooks.spec).body(authorPayload);
                break;
            case "book":
                this.bookPayload = BookDataBuilder.addBookPayload(data);
                reqSpec = given(Hooks.spec).body(bookPayload);
                break;
            default:
                throw new IllegalArgumentException("Invalid option: " + entity);
        }
    }

    @When("I send a {string} request to {string}")
    public void i_send_a_request_to(String method, String endpoint) {
        httpMethod = HttpMethod.valueOf(method.toUpperCase());
        logger.info("Sending {} request to {}", method, endpoint);
        response = RequestHelper.executeRequest(httpMethod, endpoint, reqSpec);
        logger.info("Received response with status code: {}", response.getStatusCode());
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer statusCode) {
        assertEquals(statusCode, (Integer) response.getStatusCode());
        this.statusCode = statusCode;
    }

    @Then("the response should contain the {string} details including:")
    public void the_response_should_contain_details_including(String entity,
            DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        switch (entity.toLowerCase()) {
            case "author":
                ResponseWrapper<Author> authorWrappedResponse = response.as(new TypeRef<ResponseWrapper<Author>>() {});
                ResponseValidator.validateAuthorResponse(authorWrappedResponse, data, httpMethod, this.statusCode);
                break;
            case "book":
                ResponseWrapper<Book> bookWrappedResponse = response.as(new TypeRef<ResponseWrapper<Book>>() {});
                ResponseValidator.validateBookResponse(bookWrappedResponse, data, httpMethod, this.statusCode);
                break;
            default:
                throw new IllegalArgumentException("Invalid option: " + entity);
        }
    }

    @Then("validate the response against {string} JSON schema")
    public void validate_the_response_against_the_json_schema(String schemaFile) {
        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/"+ schemaFile + ".json"));
    }

    @Given("a {string} with ID {int} exists in the system")
    public void a_entity_with_id_exists_in_the_system(String entity, Integer id) {
        //cambiar esto
        reqSpec = given(Hooks.spec);
    }

    @Given("I have valid {string} data")
    public void i_have_valid_data(String entity){
         switch (entity.toLowerCase()) {
            case "author":
                this.authorPayload = AuthorDataBuilder.addAuthorPayload();
                reqSpec = given(Hooks.spec).body(authorPayload);
                break;
            case "book":
                this.bookPayload = BookDataBuilder.addBookPayload();
                logger.info("Generated book payload: {}", bookPayload);
                reqSpec = given(Hooks.spec).body(bookPayload);
                break;
            default:
                throw new IllegalArgumentException("Invalid option: " + entity);
        }
    }

    @Then("the response should contain the {string} details")
    public void the_response_should_contain_the_details(String entity) {
        ObjectMapper mapper = new ObjectMapper();
        switch (entity.toLowerCase()) {
            case "author":
                Map<String, String> expectedAuthorData = mapper.convertValue(this.authorPayload, Map.class);
                expectedAuthorData.replaceAll((key, value) -> value != null ? value.toString() : null);
                logger.info("Expected author data: {}", expectedAuthorData);
                ResponseWrapper<Author> authorWrappedResponse = response.as(new TypeRef<ResponseWrapper<Author>>() {});
                ResponseValidator.validateAuthorResponse(authorWrappedResponse, expectedAuthorData, httpMethod, this.statusCode);
                break;
            case "book":
                Map<String, Object> rawBookData = mapper.convertValue(this.bookPayload, Map.class);
                Map<String, String> expectedBookData = new HashMap<>();
                rawBookData.forEach((key, value) -> expectedBookData.put(key, value != null ? value.toString() : null));
                logger.info("Expected book data: {}", expectedBookData);
                ResponseWrapper<Book> bookWrappedResponse = response.as(new TypeRef<ResponseWrapper<Book>>() {});
                ResponseValidator.validateBookResponse(bookWrappedResponse, expectedBookData, httpMethod, this.statusCode);
                break;
            default:
                throw new IllegalArgumentException("Invalid option: " + entity);
        }
    }

}
