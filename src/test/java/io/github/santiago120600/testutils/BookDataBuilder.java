package io.github.santiago120600.testutils;

import java.util.Map;

import com.github.javafaker.Faker;

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

    public static Book addBookPayload() {
        Faker faker = new Faker();
        Book book = new Book();
        book.setTitle(faker.book().title());
        book.setIsbn(faker.code().isbn10());
        
        book.setAisleNumber(faker.number().numberBetween(1, 100));
        book.setAuthorId(faker.number().numberBetween(1, 10));
        return book;
    }
}
