package com.greatbit.config;

import com.greatbit.model.Book;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource h2DataSource(@Value("${jdbcUrl}") String jdbcUrl) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(jdbcUrl);
        dataSource.setUser("user");
        dataSource.setPassword("password");
        return dataSource;
    }

    @Bean
    public CommandLineRunner cmd(DataSource dataSource) {
        return args -> {
            try (InputStream inputStream = this.getClass().getResourceAsStream("/initial.sql")) {
                if (inputStream == null) {
                    System.out.println("initial.sql not found");
                    return;
                }
                String sql = new String(inputStream.readAllBytes());
                try (Connection connection = dataSource.getConnection();
                     Statement statement = connection.createStatement()) {
                    statement.executeUpdate(sql);

                    // Пример вставки данных
                    String insertSql = "INSERT INTO book (pages, name, author) VALUES (?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                        preparedStatement.setInt(1, 123);
                        preparedStatement.setString(2, "JAVA book");
                        preparedStatement.setString(3, "Product star");
                        preparedStatement.executeUpdate();
                    }

                    System.out.println("Printing books from db....");
                    ResultSet rs = statement.executeQuery("SELECT book_id, pages, name, author FROM book");
                    while (rs.next()) {
                        Book book = new Book(
                                String.valueOf(rs.getInt("book_id")),
                                rs.getInt("pages"),
                                rs.getString("name"),
                                rs.getString("author")
                        );
                        System.out.println(book);
                    }
                }
            }
        };
    }
}