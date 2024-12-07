package com.aldrin.salam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    private Connection connection;

    public BookService(Connection connection) {
        this.connection = connection;
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM livres";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id_livre")); // Ensure this matches the correct column in your database
                book.setIsbn(rs.getString("isbn"));
                book.setTitle(rs.getString("titre"));
                book.setStock(rs.getInt("disponible"));
                books.add(book);
            }
        }
        return books;
    }

    public int getBookIdByTitle(String title) throws SQLException {
        String query = "SELECT id_livre FROM livres WHERE titre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_livre");
            } else {
                throw new SQLException("Book not found");
            }
        }
    }
    
}