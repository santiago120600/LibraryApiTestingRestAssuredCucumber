package io.github.santiago120600.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Author{
    Integer id;
    @JsonProperty("first_name")
    String firstName;
    @JsonProperty("last_name")
    String lastName;
}
