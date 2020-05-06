package com.kindle.dao;

import org.springframework.jdbc.core.JdbcTemplate;

public interface KindleStore {
    String SQL_SELECT_BOOK_PRICE = "SELECT price FROM book WHERE isbn = ?";
    String SQL_UPDATE_BOOK_STOCK = "UPDATE book_stock SET stock = stock - 1 " +
            "WHERE isbn = ?";
    String SQL_UPDATE_ACCOUNT_BALANCE = "UPDATE amazon_pay SET balance = balance - ? " +
            "WHERE username = ?";

    void purchase(String isbn, String userName);

    JdbcTemplate getJdbcTemplate();

    default void purchaseDaoActions(String isbn, String userName) {
        int price = getJdbcTemplate().queryForObject(
                SQL_SELECT_BOOK_PRICE, Integer.class, isbn);

        getJdbcTemplate().update(
                SQL_UPDATE_BOOK_STOCK, isbn);

        getJdbcTemplate().update(
                SQL_UPDATE_ACCOUNT_BALANCE, price, userName);
    }
}
