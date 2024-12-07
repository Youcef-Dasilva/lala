package com.aldrin.salam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanService {
    private Connection connection;

    public LoanService(Connection connection) {
        this.connection = connection;
    }

    public void addLoan(Loan loan) throws SQLException {
        String sql = "INSERT INTO Emprunts (id_membre, id_livre, date_emprunt, date_retour, retourne) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, loan.getMemberId());
            pstmt.setInt(2, loan.getBookId());
            pstmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            pstmt.setDate(4, null);
            pstmt.setBoolean(5, false);
            pstmt.executeUpdate();
        }
        decreaseBookStock(loan.getBookId());
    }

    public void returnLoan(int loanId, int bookId) throws SQLException {
        String sql = "UPDATE Emprunts SET retourne = TRUE, date_retour = ? WHERE id_emprunt = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
            pstmt.setInt(2, loanId);
            pstmt.executeUpdate();
        }
        increaseBookStock(bookId);
    }

    private void decreaseBookStock(int bookId) throws SQLException {
        String sql = "UPDATE livres SET disponible = disponible - 1 WHERE id_livre = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        }
    }

    private void increaseBookStock(int bookId) throws SQLException {
        String sql = "UPDATE livres SET disponible = disponible + 1 WHERE id_livre = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        }
    }

    public List<Loan> getAllLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM Emprunts";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getInt("id_emprunt"));
                loan.setMemberId(rs.getInt("id_membre"));
                loan.setBookId(rs.getInt("id_livre"));
                loan.setLoanDate(rs.getDate("date_emprunt").toString());
                loan.setReturnDate(rs.getDate("date_retour") != null ? rs.getDate("date_retour").toString() : null);
                loan.setReturned(rs.getBoolean("retourne"));
                loans.add(loan);
            }
        }
        return loans;
    }
}