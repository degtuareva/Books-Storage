package com.greatbit.dao;

import com.greatbit.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookDaoImpl implements BookDao {
    private final DataSource dataSource;

    @Autowired
    public BookDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Book> findAll() {
        String selectSQL = "SELECT book_id, pages, name, author FROM book";
        List<Book> books = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(selectSQL)) {

            while (rs.next()) {
                Book book = new Book(
                        String.valueOf(rs.getInt("book_id")),
                        rs.getInt("pages"),
                        rs.getString("name"),
                        rs.getString("author")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при чтении книг из БД", e);
        }
        return books;
    }

    @Override
    public Book save(Book book) {
        String insertSQL = "INSERT INTO book (pages, name, author) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, book.getPages());
            preparedStatement.setString(2, book.getName());
            preparedStatement.setString(3, book.getAuthor());
            preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    book.setId(String.valueOf(rs.getInt(1)));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }

    @Override
    public Book getById(Integer bookId) {
        String getByIdSql = "SELECT book_id, pages, name, author FROM book WHERE book_id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getByIdSql)) {

            preparedStatement.setInt(1, bookId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.next()) {
                    throw new RuntimeException(String.format("Book with id %d was not found", bookId));
                }
                return new Book(
                        String.valueOf(rs.getInt("book_id")),
                        rs.getInt("pages"),
                        rs.getString("name"),
                        rs.getString("author")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book update(Book book) {
        String updateSQL = "UPDATE book SET pages=?, name=?, author=? WHERE book_id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setInt(1, book.getPages());
            preparedStatement.setString(2, book.getName());
            preparedStatement.setString(3, book.getAuthor());
            preparedStatement.setInt(4, Integer.parseInt(book.getId()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }

    @Override
    public void delete(int bookId) {
        String deleteSQL = "DELETE FROM book WHERE book_id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, bookId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        String deleteSQL = "TRUNCATE TABLE book";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(deleteSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении всех записей", e);
        }
    }
}