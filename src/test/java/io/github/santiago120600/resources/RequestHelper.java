package io.github.santiago120600.resources;

import io.github.santiago120600.enums.HttpMethod;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RequestHelper {

    public static Response executeRequest(HttpMethod method, String endpoint, RequestSpecification reqSpec) {
        switch (method) {
            case POST:
                return reqSpec.when().post(endpoint);
            case GET:
                return reqSpec.when().get(endpoint);
            case PUT:
                return reqSpec.when().put(endpoint);
            case DELETE:
                return reqSpec.when().delete(endpoint);
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }
}
