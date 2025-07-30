package io.github.santiago120600.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Book{
   String title;
   Integer id; 
   @JsonProperty("aisle_number")
   Integer aisleNumber;
   String isbn;
   Author author;
   @JsonProperty("author_id") 
   Integer authorId;
}
