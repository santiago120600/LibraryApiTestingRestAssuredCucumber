package io.github.santiago120600.stepDefinitions;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.santiago120600.models.Book;
import io.github.santiago120600.resources.BookDataBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class StepDefinition {

    RequestSpecification reqSpec;
    Response response;
    String httpMethod;

    @Given("I have the following valid book data:")
    public void i_have_the_following_valid_book_data(DataTable dataTable) throws IOException {
        Map<String, String> bookData = dataTable.asMaps().get(0);
        Book book = BookDataBuilder.addBookPayload(bookData);
        reqSpec = given(Hooks.spec).body(book);
    }

    @When("I send a {string} request to {string}")
    public void i_send_a_request_to(String httpMethod, String endpoint) {
        this.httpMethod = httpMethod;
        if (httpMethod.equalsIgnoreCase("POST")) {
            response = reqSpec.when().post(endpoint);
        } else if (httpMethod.equalsIgnoreCase("GET")) {
            response = reqSpec.when().get(endpoint);
        } else if (httpMethod.equalsIgnoreCase("DELETE")) {
            response = reqSpec.when().delete(endpoint);
        }else if (httpMethod.equalsIgnoreCase("PUT")) {
            response = reqSpec.when().put(endpoint);
        } else {
            throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
        }
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
        assertEquals(bookData.get("isbn"), book.getIsbn());
        assertEquals(bookData.get("book_name"), book.getBook_name());
        assertEquals(bookData.get("authorId"), book.getAuthor().getId().toString());
        assertEquals(bookData.get("aisle"), book.getAisle().toString());
        assertNotNull(book.getId());
        assertNotNull(book.getAuthor().getName());
        if(this.httpMethod.equalsIgnoreCase("POST")){
            assertEquals("Book successfully added",book.getMessage());
        }else if(this.httpMethod.equalsIgnoreCase("GET")){
            assertEquals("OK",book.getMessage());
        }else if(this.httpMethod.equalsIgnoreCase("PUT")){
            assertEquals("Book updated successfully",book.getMessage());
        }else if(this.httpMethod.equalsIgnoreCase("DELETE")){
            assertEquals("Book deleted successfully",book.getMessage());
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
