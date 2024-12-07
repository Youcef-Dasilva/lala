package com.aldrin.salam;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;

public class LoanController {

    @FXML
    private TableView<Loan> loansTable;
    @FXML
    private TextField loanBookIdField;
    @FXML
    private TextField loanUserIdField;
    private LoanService loanService;

    public LoanController() {
        try {
            this.loanService = new LoanService(Database.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        loansTable.setItems(getAllLoans());
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
        Loan loan = new Loan();
        loan.setBookId(Integer.parseInt(loanBookIdField.getText()));
        loan.setMemberId(Integer.parseInt(loanUserIdField.getText()));
        try {
            loanService.addLoan(loan);
            loansTable.setItems(getAllLoans());
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}