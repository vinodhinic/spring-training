package com.kindle.dao;

import com.kindle.domain.AmazonPay;
import com.kindle.domain.Book;
import com.kindle.domain.BookStock;

import java.util.List;

public interface KindleDao {
    boolean insertBook(Book book);

    boolean insertBookStock(BookStock bookStock);

    boolean insertAccount(AmazonPay amazonPay);

    void deleteAllBookAndStock();

    void deleteAllAccounts();

    List<Book> selectAllBooks();

    List<BookStock> selectAllBookStocks();

    List<AmazonPay> selectAllAmazonPay();
}
