package com.kindle;

import java.sql.*;

public class KindleStoreImpl implements KindleStore {

    private static final String SQL_SELECT_BOOK_PRICE = "SELECT price FROM book WHERE isbn = ?";
    private static final String SQL_UPDATE_BOOK_STOCK = "UPDATE book_stock SET stock = stock - 1 " +
            "WHERE isbn = ?";
    private static final String SQL_UPDATE_ACCOUNT_BALANCE = "UPDATE amazon_pay SET balance = balance - ? " +
            "WHERE username = ?";

    @Override
    public boolean purchase(String isbn, String username) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres", "postgres", "admin");
            conn.setAutoCommit(false);

            PreparedStatement stmt1 = conn.prepareStatement(
                    SQL_SELECT_BOOK_PRICE);

            stmt1.setString(1, isbn);
            ResultSet rs = stmt1.executeQuery();
            rs.next();
            int price = rs.getInt("PRICE");

            PreparedStatement stmt2 = conn.prepareStatement(SQL_UPDATE_BOOK_STOCK);
            stmt2.setString(1, isbn);
            stmt2.executeUpdate();

            PreparedStatement stmt3 = conn.prepareStatement(SQL_UPDATE_ACCOUNT_BALANCE);
            stmt3.setInt(1, price);
            stmt3.setString(2, username);
            stmt3.executeUpdate();
            stmt3.close();
            conn.commit();
        } catch (Throwable e) {
            // e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
