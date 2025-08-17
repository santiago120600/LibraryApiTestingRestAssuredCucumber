package io.github.santiago120600.testutils;

import java.io.IOException;
import java.io.InputStream;

import org.assertj.core.api.SoftAssertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.santiago120600.enums.HttpMethod;
import io.github.santiago120600.models.Author;
import io.github.santiago120600.models.Book;
import io.github.santiago120600.models.ResponseWrapper;

public class ResponseValidator {

    private static final Logger logger = LoggerFactory.getLogger(ResponseValidator.class);

    public static void validateAuthorResponse(ResponseWrapper<Author> wrappedResponse, Author expectedData,
            HttpMethod httpMethod, Integer statusCode) {
        Author author = wrappedResponse.getData();
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(wrappedResponse.getStatus())
                .as("Status validation")
                .isEqualTo("success");
        soft.assertThat(author.getId())
                .as("Author ID validation")
                .isNotNull();
        soft.assertThat(author.getFirstName())
                .as("Author first name validation: expected <%s> but was <%s>", expectedData.getFirstName(), author.getFirstName()).
                isEqualTo(expectedData.getFirstName());
        soft.assertThat(author.getLastName())
                .as("Author last name validation: expected <%s> but was <%s>", expectedData.getLastName(),
                        author.getLastName())
                .isEqualTo(expectedData.getLastName());
        soft.assertThat(wrappedResponse.getMessage())
                .as("Response message validation")
                .isEqualTo(getExpectedMessage(httpMethod, "Author", statusCode));
        soft.assertAll();
    }

    public static void validateBookResponse(ResponseWrapper<Book> wrappedResponse, Book expectedData,
            HttpMethod httpMethod, Integer statusCode) {
        Book book = wrappedResponse.getData();
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(wrappedResponse.getStatus())
                .as("Status validation")
                .isEqualTo("success");
        soft.assertThat(book.getId())
                .as("Book ID validation")
                .isNotNull();
        soft.assertThat(book.getTitle())
                .as("Book title validation: expected <%s> but was <%s>", expectedData.getTitle(),
                        book.getTitle())
                .isEqualTo(expectedData.getTitle());
        soft.assertThat(book.getIsbn())
                .as("ISBN validation: expected <%s> but was <%s>", expectedData.getIsbn(), book.getIsbn())
                .isEqualTo(expectedData.getIsbn());
        soft.assertThat(book.getAisleNumber())
                .as("Aisle number validation: expected <%s> but was <%s>", expectedData.getAisleNumber(),
                        book.getAisleNumber())
                .isEqualTo(expectedData.getAisleNumber());
        soft.assertThat(book.getAuthor().getId())
                .as("Author ID validation: expected <%s> but was <%s>", expectedData.getAuthorId(),
                        book.getAuthor().getId())
                .isEqualTo(expectedData.getAuthorId());
        soft.assertThat(book.getAuthor().getFirstName())
                .as("Author first name validation: expected <%s> but was <%s>", expectedData.getAuthor().getFirstName(),
                        book.getAuthor().getFirstName())
                .isEqualTo(expectedData.getAuthor().getFirstName());
        soft.assertThat(book.getAuthor().getLastName())
                .as("Author last name validation: expected <%s> but was <%s>", expectedData.getAuthor().getLastName(),
                        book.getAuthor().getLastName())
                .isEqualTo(expectedData.getAuthor().getLastName());
        soft.assertThat(wrappedResponse.getMessage())
                .as("Response message validation")
                .isEqualTo(getExpectedMessage(httpMethod, "Book", statusCode));
        soft.assertAll();
    }

    private static String getExpectedMessage(HttpMethod method, String entity, Integer statusCode) {
        if (method == null || entity == null || statusCode == null) {
            throw new IllegalArgumentException("Method, entity, and statusCode must not be null");
        }
        String entityKey = entity.toLowerCase();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InputStream inputStream = ResponseValidator.class.getClassLoader()
                    .getResourceAsStream("response-messages.json");
            if (inputStream == null) {
                throw new IOException("Resource file 'response-messages.json' not found in classpath");
            }
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode endpoints = rootNode.path("api").path("endpoints");
            for (JsonNode endpoint : endpoints) {
                if (endpoint.path("name").asText().equalsIgnoreCase(entityKey)) {
                    JsonNode methods = endpoint.path("methods");
                    JsonNode methodResponses = methods.path(method.name());
                    for (JsonNode response : methodResponses) {
                        if (response.path("status_code").asInt() == statusCode) {
                            logger.info("Found response message for {} {} with status code {}", method, entity,
                                    statusCode);
                            String message = response.path("message").asText();
                            logger.info("Response message: {}", message);
                            return message;
                        }
                    }
                }
            }
            return "";
        } catch (Exception e) {
            throw new RuntimeException("Error reading response messages JSON", e);
        }
    }
}
