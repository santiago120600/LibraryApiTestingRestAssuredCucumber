package io.github.santiago120600.stepDefinitions;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import io.github.santiago120600.testutils.Const;
import io.github.santiago120600.testutils.RequestHelper;
import io.github.santiago120600.testutils.ResponseValidator;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class StepDefinition {

    private static final Logger logger = LoggerFactory.getLogger(StepDefinition.class);
    RequestSpecification reqSpec;
    Response response;
    HttpMethod httpMethod;
    Integer statusCode;
    private Author authorPayload;
    private Book bookPayload;
    private Integer authorId;
    private Integer bookId;

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
                if(this.authorId != null){
                    this.bookPayload.setAuthorId(this.authorId);
                }
                reqSpec = given(Hooks.spec).body(bookPayload);
                break;
            default:
                throw new IllegalArgumentException("Invalid option: " + entity);
        }
    }

    @When("I send a {string} request to {string}")
    public void i_send_a_request_to(String method, String endpoint) {
        httpMethod = HttpMethod.valueOf(method.toUpperCase());
        // Replace <id> placeholder with actual bookId or authorId if available
        if (endpoint.contains("<id>")) {
            if (endpoint.contains("/books") && bookId != null) {
                endpoint = endpoint.replace("<id>", bookId.toString());
            } else if (endpoint.contains("/authors") && authorId != null) {
                endpoint = endpoint.replace("<id>", authorId.toString());
            } else {
                throw new IllegalStateException("No valid ID available for endpoint: " + endpoint);
            }
        }
        logger.info("Sending {} request to {}", method, endpoint);
        if(method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("DELETE")) {
            reqSpec = given(Hooks.spec);
        }
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
                Author expectedAuthor = new Author();
                expectedAuthor.setFirstName(data.get(Const.AUTHOR_FIRSTNAME));
                expectedAuthor.setLastName(data.get(Const.AUTHOR_LASTNAME));
                ResponseValidator.validateAuthorResponse(authorWrappedResponse, expectedAuthor, httpMethod, this.statusCode);
                break;
            case "book":
                ResponseWrapper<Book> bookWrappedResponse = response.as(new TypeRef<ResponseWrapper<Book>>() {});
                Book expectedBook = new Book();
                expectedBook.setTitle(data.get(Const.BOOK_NAME));
                expectedBook.setIsbn(data.get(Const.ISBN));
                expectedBook.setAisleNumber(Integer.parseInt(data.get(Const.AISLE)));
                Author bookAuthor = new Author();
                if(this.authorId != null){
                    bookAuthor.setId(this.authorId);
                    bookAuthor.setFirstName(this.authorPayload.getFirstName());
                    bookAuthor.setLastName(this.authorPayload.getLastName());
                } else if(data.containsKey(Const.AUTHOR_ID)){
                    bookAuthor.setId(Integer.parseInt(data.get(Const.AUTHOR_ID)));
                    bookAuthor.setFirstName(data.get(Const.AUTHOR_FIRSTNAME));
                    bookAuthor.setLastName(data.get(Const.AUTHOR_LASTNAME));
                }
                expectedBook.setAuthor(bookAuthor);
                ResponseValidator.validateBookResponse(bookWrappedResponse, expectedBook, httpMethod, this.statusCode);
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
        reqSpec = given(Hooks.spec);
    }
    
    @Given("a {string} is created in the system")
    public void a_entity_with_is_created_in_the_system(String entity) {
        switch (entity.toLowerCase()) {
            case "author":
                this.authorPayload = AuthorDataBuilder.addAuthorPayload();
                reqSpec = given(Hooks.spec).body(authorPayload);
                response = RequestHelper.executeRequest(HttpMethod.POST, "/authors", reqSpec);
                ResponseWrapper<Author> wrappedAuthor = response.as(new TypeRef<ResponseWrapper<Author>>() {});
                this.authorId = wrappedAuthor.getData().getId();
                break;
            case "book":
                this.authorPayload = AuthorDataBuilder.addAuthorPayload();
                reqSpec = given(Hooks.spec).body(authorPayload);
                response = RequestHelper.executeRequest(HttpMethod.POST, "/authors", reqSpec);
                wrappedAuthor = response.as(new TypeRef<ResponseWrapper<Author>>() {});
                Integer createdAuthorId = wrappedAuthor.getData().getId();
                this.authorId = createdAuthorId;
                this.bookPayload = BookDataBuilder.addBookPayload();
                this.bookPayload.setAuthorId(createdAuthorId);
                reqSpec = given(Hooks.spec).body(bookPayload);
                response = RequestHelper.executeRequest(HttpMethod.POST, "/books", reqSpec);
                ResponseWrapper<Book> wrappedBook = response.as(new TypeRef<ResponseWrapper<Book>>() {});
                this.bookId = wrappedBook.getData().getId();
                break;
            default:
                throw new IllegalArgumentException("Invalid option: " + entity);
        }
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
                if(this.authorId != null){
                    this.bookPayload.setAuthorId(this.authorId);
                }
                reqSpec = given(Hooks.spec).body(bookPayload);
                break;
            default:
                throw new IllegalArgumentException("Invalid option: " + entity);
        }
    }

    @Then("the response should contain the {string} details")
    public void the_response_should_contain_the_details(String entity) {
        switch (entity.toLowerCase()) {
            case "author":
                ResponseWrapper<Author> authorWrappedResponse = response.as(new TypeRef<ResponseWrapper<Author>>() {});
                ResponseValidator.validateAuthorResponse(authorWrappedResponse, this.authorPayload, httpMethod, this.statusCode);
                break;
            case "book":
                ResponseWrapper<Book> bookWrappedResponse = response.as(new TypeRef<ResponseWrapper<Book>>() {});
                if(this.authorPayload != null && this.authorId != null){
                    this.bookPayload.setAuthor(authorPayload);
                }
                ResponseValidator.validateBookResponse(bookWrappedResponse, this.bookPayload, httpMethod, this.statusCode);
                break;
            default:
                throw new IllegalArgumentException("Invalid option: " + entity);
        }
    }

}
