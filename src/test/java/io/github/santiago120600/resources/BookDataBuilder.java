package io.github.santiago120600.resources;

import java.util.Map;

import io.github.santiago120600.models.Book;

public class BookDataBuilder {
    

    public static Book addBookPayload(Map<String, String> bookData) {
        Book book = new Book();
        book.setTitle(bookData.get(Const.BOOK_NAME));
        book.setIsbn(bookData.get(Const.ISBN));
        book.setAisleNumber(Integer.parseInt(bookData.get(Const.AISLE)));
        book.setAuthorId(Integer.parseInt(bookData.get(Const.AUTHOR_ID)));
        return book;
    }
}
