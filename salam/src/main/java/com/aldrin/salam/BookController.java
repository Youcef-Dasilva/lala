package com.aldrin.salam;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import java.sql.SQLException;
import java.util.List;

public class BookController {

    @FXML
    private TableView<Book> booksTable;
    private BookService bookService;

    public BookController() {
        try {
            this.bookService = new BookService(Database.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        booksTable.setItems(getAllBooks());
    }

    private ObservableList<Book> getAllBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            return FXCollections.observableArrayList(books);
        } catch (SQLException e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }
}