package io.github.santiago120600.stepDefinitions;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.santiago120600.enums.HttpMethod;
import io.github.santiago120600.models.Book;
import io.github.santiago120600.resources.BookDataBuilder;
import io.github.santiago120600.resources.Const;
import io.github.santiago120600.resources.RequestHelper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class StepDefinition {

    RequestSpecification reqSpec;
    Response response;
    HttpMethod httpMethod;

    @Given("I have the following valid book data:")
    public void i_have_the_following_valid_book_data(DataTable dataTable) throws IOException {
        Map<String, String> bookData = dataTable.asMaps().get(0);
        Book book = BookDataBuilder.addBookPayload(bookData);
        reqSpec = given(Hooks.spec).body(book);
    }

    @When("I send a {string} request to {string}")
    public void i_send_a_request_to(String method, String endpoint) {
        httpMethod = HttpMethod.valueOf(method.toUpperCase());
        response = RequestHelper.executeRequest(httpMethod, endpoint, reqSpec);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer statusCode) {
        assertEquals(statusCode, (Integer) response.getStatusCode());
    }

    @Then("the response should contain the book details including:")
    public void the_response_should_contain_the_created_book_details_including(
            DataTable dataTable) {
        Map<String, String> bookData = dataTable.asMaps().get(0);
        Book book = response.as(Book.class);
        assertEquals(bookData.get(Const.ISBN), book.getIsbn());
        assertEquals(bookData.get(Const.BOOK_NAME), book.getTitle());
        assertEquals(bookData.get(Const.AUTHOR_ID), book.getAuthor().getId().toString());
        assertEquals(bookData.get(Const.AISLE), book.getAisleNumber().toString());
        assertNotNull(book.getId());
        assertNotNull(book.getAuthor().getName());
        switch (httpMethod) {
            case POST:
                assertEquals("Book added successfully", book.getMessage());
                break;
                case GET: 
                assertEquals("OK", book.getMessage());
                break;
                case PUT: 
                assertEquals("Book updated successfully", book.getMessage()); 
                break;
                case DELETE: 
                assertEquals("Book deleted successfully", book.getMessage());
                break;
            default: throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
        }
    }

    @Then("validate the response against {string} JSON schema")
    public void validate_the_response_against_the_json_schema(String schemaFile) {
        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/"+ schemaFile + ".json"));
    }

    @Given("a book with ID {int} exists in the system")
    public void a_book_with_id_exists_in_the_system(Integer id) {
        reqSpec = given(Hooks.spec);
    }

}
