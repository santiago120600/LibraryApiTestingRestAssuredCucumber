package io.github.santiago120600.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Book{
   String book_name;
   Integer id; 
   Integer aisle;
   String isbn;
   Author author;
   Integer authorId;
}
