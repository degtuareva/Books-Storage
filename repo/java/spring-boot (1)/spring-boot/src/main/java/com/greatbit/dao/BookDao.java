package com.greatbit.dao;

import com.greatbit.model.Book;

import java.util.List;

public interface BookDao {
    List<Book> findAll();

    Book save(Book book);

    Book getById(Integer bookId);

    Book update(Book book);

    void delete(int bookId);

    void deleteAll();
}