package io.github.santiago120600.testutils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

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


    public static void validateAuthorResponse(ResponseWrapper<Author> wrappedResponse, Map<String, String> expectedData,
            HttpMethod httpMethod, Integer statusCode) {
        Author author = wrappedResponse.getData();
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(wrappedResponse.getStatus()).isEqualTo("success");
        soft.assertThat(author.getFirstName()).isEqualTo(expectedData.get(Const.AUTHOR_FIRSTNAME));
        soft.assertThat(author.getLastName()).isEqualTo(expectedData.get(Const.AUTHOR_LASTNAME));
        soft.assertThat(author.getId()).isNotNull();
        soft.assertThat(wrappedResponse.getMessage()).isEqualTo(getExpectedMessage(httpMethod, "Author", statusCode));
        soft.assertAll();
    }

    public static void validateBookResponse(ResponseWrapper<Book> wrappedResponse, Map<String, String> expectedData,
            HttpMethod httpMethod, Integer statusCode) {
        Book book = wrappedResponse.getData();
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(wrappedResponse.getStatus()).isEqualTo("success");
        soft.assertThat(book.getId()).isNotNull();
        soft.assertThat(book.getTitle()).isEqualTo(expectedData.get(Const.BOOK_NAME));
        soft.assertThat(book.getIsbn()).isEqualTo(expectedData.get(Const.ISBN));
        soft.assertThat(book.getAisleNumber()).isEqualTo(Integer.parseInt(expectedData.get(Const.AISLE)));
        soft.assertThat(book.getAuthor().getId()).isEqualTo(Integer.parseInt(expectedData.get(Const.AUTHOR_ID)));
        soft.assertThat(book.getAuthor().getFirstName()).isNotNull();
        soft.assertThat(book.getAuthor().getLastName()).isNotNull();
        soft.assertThat(wrappedResponse.getMessage()).isEqualTo(getExpectedMessage(httpMethod, "Book", statusCode));
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
                            logger.info("Found response message for {} {} with status code {}", method, entity, statusCode);
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
