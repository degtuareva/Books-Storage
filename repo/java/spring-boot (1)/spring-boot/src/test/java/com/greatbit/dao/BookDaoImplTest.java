package com.greatbit.dao;

import com.greatbit.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@SpringBootTest(
        properties = {"jdbcUrl=jdbc:h2:mem:db;DB_CLOSE_DELAY=-1"}
)
class BookDaoImplTest {

    @Autowired
    private BookDao bookDao;

    @BeforeEach
    public void beforeEach() {
        bookDao.deleteAll();
    }

    @Test
    public void contextCreated() {

    }

    @Test
    public void saveSavesDataTToDbReturnEntityWithId() throws SQLException {
        final Book book = bookDao.save(new Book(null, 123, "name 1", "author"));
        assertThat(book.getId()).isNotNull();
        assertThat(bookDao.findAll())
                .isNotNull()
                .extracting("id")
                .contains(book.getId());
    }

    @Test
    void deleteAllDeletesAllData() throws SQLException {
        bookDao.save(new Book(null, 123, "name 2", "author"));
        assertThat(bookDao.findAll()).isNotEmpty();
        bookDao.deleteAll();
        assertThat(bookDao.findAll()).isEmpty();
    }

    @Test
    void findAllReturnAllBooks() throws SQLException {
        assertThat(bookDao.findAll()).isEmpty();
        bookDao.save(new Book(null, 123, "name 3", "author"));
        assertThat(bookDao.findAll()).isNotEmpty();
    }

    @Test
    void getByIdThrowsRuntimeExceptionIfNoBookNotFound() throws SQLException {
        assertThatThrownBy(() -> bookDao.getById(123))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("was not found");
    }

    @Test
    void getByIdReturnsCorrectBook() throws SQLException {
        Book book = bookDao.save(new Book(null, 123, "name 3", "author"));
        assertThat(bookDao.getById(Integer.parseInt(book.getId())))
                .isNotNull()
                .extracting("id")
                .isEqualTo(book.getId());
    }

    @Test
    void updateUpdatesDataInDb() throws SQLException {
        Book book = bookDao.save(new Book(null, 123, "name 1", "author"));
        book.setName("Updated Name");
        bookDao.update(book);
        assertThat(bookDao.getById(Integer.parseInt(book.getId())).getName())
                .isEqualTo("Updated Name");
    }
}