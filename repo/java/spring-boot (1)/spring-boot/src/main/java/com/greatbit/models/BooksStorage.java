package com.greatbit.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BooksStorage {
    private static Set<Book> books = new HashSet<>();
    static {
        books.add(
                new Book(UUID.randomUUID().toString(),
                        "Вon Juan teachings",
                        "Karlos Kastaneda",
                        400)
        );
        books.add(
                new Book(
                        UUID.randomUUID().toString(),
                        "Rich dad Poor dad",
                        "Robert Kiosaki",
                        500)
        );
    }
    public static Set<Book> getBooks() {
        return books;
    }
}
