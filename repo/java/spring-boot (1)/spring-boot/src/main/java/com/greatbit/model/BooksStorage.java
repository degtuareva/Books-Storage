package com.greatbit.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BooksStorage {
    private static Set<Book> books = new HashSet<>();

    static {
        books.add(
                new Book(UUID.randomUUID().toString(),
                        400,
                        "Karlos Kastaneda",
                        "Вon Juan teachings")
        );
        books.add(
                new Book(
                        UUID.randomUUID().toString(),
                        500,
                        "Robert Kiosaki",
                        "Rich dad Poor dad")
        );
        books.add(
                new Book(
                        UUID.randomUUID().toString(),
                        2000,
                        "Философия JAVA",
                        "Брюс Эккель")
        );
    }

    public static Set<Book> getBooks() {
        return books;
    }
}