package com.aldrin.salam;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserController {

    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TableView<Loan> loansTable;
    @FXML
    private TextField loanBookTitleField;
    @FXML
    private TextField loanUsernameField;
    private BookService bookService;
    private LoanService loanService;
    private UserService userService;

    public UserController() {
        try {
            this.bookService = new BookService(Database.getConnection());
            this.loanService = new LoanService(Database.getConnection());
            this.userService = new UserService(Database.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        initializeBooksTable();
        initializeLoansTable();
        booksTable.setItems(getAllBooks());
        loansTable.setItems(getAllLoans());
    }

    private void initializeBooksTable() {
        TableColumn<Book, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        booksTable.getColumns().addAll(idColumn, isbnColumn, titleColumn, stockColumn);
    }

    private void initializeLoansTable() {
        TableColumn<Loan, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Loan, Integer> memberIdColumn = new TableColumn<>("Member ID");
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));

        TableColumn<Loan, Integer> bookIdColumn = new TableColumn<>("Book ID");
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<Loan, String> loanDateColumn = new TableColumn<>("Loan Date");
        loanDateColumn.setCellValueFactory(new PropertyValueFactory<>("loanDate"));

        TableColumn<Loan, String> returnDateColumn = new TableColumn<>("Return Date");
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        TableColumn<Loan, Boolean> returnedColumn = new TableColumn<>("Returned");
        returnedColumn.setCellValueFactory(new PropertyValueFactory<>("returned"));

        loansTable.getColumns().addAll(idColumn, memberIdColumn, bookIdColumn, loanDateColumn, returnDateColumn, returnedColumn);
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

    private ObservableList<Loan> getAllLoans() {
        try {
            return FXCollections.observableArrayList(loanService.getAllLoans());
        } catch (SQLException e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    @FXML
    private void handleBorrowBook() {
        String bookTitle = loanBookTitleField.getText();
        String username = loanUsernameField.getText();
        try {
            int bookId = bookService.getBookIdByTitle(bookTitle); // Ensure this method gets the correct book ID
            int userId = userService.getUserIdByUsername(username);
            Loan loan = new Loan();
            loan.setBookId(bookId); // Set the bookId to match the book's ID
            loan.setMemberId(userId);
            loanService.addLoan(loan);
            loansTable.setItems(getAllLoans());
            booksTable.setItems(getAllBooks());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReturnBook() {
        Loan selectedLoan = loansTable.getSelectionModel().getSelectedItem();
        if (selectedLoan != null) {
            try {
                loanService.returnLoan(selectedLoan.getId(), selectedLoan.getBookId());
                loansTable.setItems(getAllLoans());
                booksTable.setItems(getAllBooks());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void switchToPrimary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) booksTable.getScene().getWindow(); // Assuming booksTable is in the current scene
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }}