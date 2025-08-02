package io.github.santiago120600.testutils;

import java.util.Map;

import com.github.javafaker.Faker;

import io.github.santiago120600.models.Author;

public class AuthorDataBuilder {

    public static Author addAuthorPayload(Map<String, String> data) {
        Author author = new Author();
        author.setFirstName(data.get(Const.AUTHOR_FIRSTNAME));
        author.setLastName(data.get(Const.AUTHOR_LASTNAME));
        return author;
    }

    public static Author addAuthorPayload() {
        Faker faker = new Faker();
        Author author = new Author();
        author.setFirstName(faker.name().firstName());
        author.setLastName(faker.name().lastName());
        return author;
    }
}
