package io.github.santiago120600.testutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class ExtentRestAssured implements Filter{

    private static final Logger logger = LoggerFactory.getLogger(ExtentRestAssured.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        ExtentTest test = com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter.getCurrentStep();

        if (test != null) {
            try {
                StringBuilder requestDetails = new StringBuilder();
                requestDetails.append("Request method: ").append(requestSpec.getMethod()).append("\n");
                requestDetails.append("Request URI:    ").append(requestSpec.getURI()).append("\n");
                requestDetails.append("Proxy:          ");
                if( requestSpec.getProxySpecification() == null) {
                    requestDetails.append("<none>\n");
                } else {
                    requestDetails.append(requestSpec.getProxySpecification().toString()).append("\n");
                }
                
                requestDetails.append("Request params: ");
                if (requestSpec.getRequestParams().isEmpty()) {
                    requestDetails.append("<none>\n");
                } else {
                    requestDetails.append(requestSpec.getRequestParams().toString()).append("\n");
                }

                requestDetails.append("Query params:   ");
                if (requestSpec.getQueryParams().isEmpty()) {
                    requestDetails.append("<none>\n");
                } else {
                    requestDetails.append(requestSpec.getQueryParams().toString()).append("\n");
                }

                requestDetails.append("Form params:    ");
                if (requestSpec.getFormParams().isEmpty()) {
                    requestDetails.append("<none>\n");
                } else {
                    requestDetails.append(requestSpec.getFormParams().toString()).append("\n");
                }

                requestDetails.append("Path params:    ");
                if (requestSpec.getPathParams().isEmpty()) {
                    requestDetails.append("<none>\n");
                } else {
                    requestDetails.append(requestSpec.getPathParams().toString()).append("\n");
                }

                requestDetails.append("Headers:        ");
                requestSpec.getHeaders().asList().forEach(header -> 
                    requestDetails.append(header.getName()).append("=").append(header.getValue()).append("\n                "));
                
                requestDetails.append("Cookies:        ");
                if (requestSpec.getCookies().asList().isEmpty()) {
                    requestDetails.append("<none>\n");
                } else {
                    requestDetails.append(requestSpec.getCookies().toString()).append("\n");
                }

                requestDetails.append("Multiparts:     ");
                if (requestSpec.getMultiPartParams().isEmpty()) {
                    requestDetails.append("<none>\n");
                } else {
                    requestDetails.append(requestSpec.getMultiPartParams().toString()).append("\n");
                }

                if (requestSpec.getBody() != null) {
                    requestDetails.append("Body:\n").append(formatJson(requestSpec.getBody().toString())).append("\n");
                }

                // Log request to Extent Report
                test.info(MarkupHelper.createCodeBlock(requestDetails.toString()));

                // Format response details
                StringBuilder responseDetails = new StringBuilder();
                responseDetails.append(response.getStatusLine()).append("\n");
                response.getHeaders().asList().forEach(header -> 
                    responseDetails.append(header.getName()).append(": ").append(header.getValue()).append("\n"));
                responseDetails.append("\n");
                responseDetails.append(formatJson(response.getBody().asString()));

                // Log response to Extent Report
                test.info(MarkupHelper.createCodeBlock(responseDetails.toString()));
            } catch (Exception e) {
                logger.error("Error logging request/response to Extent Report", e);
            }
        } else {
            logger.warn("ExtentTest instance is null, cannot log request/response details");
        }

        return response;
    }

    private String formatJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object jsonObject = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (Exception e) {
            logger.warn("Could not format JSON, returning as is: {}", json, e);
            return json;
        }
    }

}
