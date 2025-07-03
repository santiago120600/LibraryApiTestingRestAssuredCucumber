package io.github.santiago120600.resources;

import java.util.Map;

import io.github.santiago120600.models.Book;

public class BookDataBuilder {

    public static Book addBookPayload(Map<String, String> bookData) {
        Book book = new Book();
        book.setBook_name(bookData.get("book_name"));
        book.setIsbn(bookData.get("isbn"));
        book.setAisle(Integer.parseInt(bookData.get("aisle")));
        book.setAuthorId(Integer.parseInt(bookData.get("authorId")));
        return book;
    }
}
