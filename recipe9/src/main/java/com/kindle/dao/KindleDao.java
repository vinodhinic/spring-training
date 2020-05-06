package com.kindle.dao;

import com.kindle.domain.Book;
import com.kindle.domain.BookStock;

import java.util.List;

public interface KindleDao {
    boolean insertBook(Book book);

    boolean insertBookStock(BookStock bookStock);

    void deleteAllBookAndStock();

    List<Book> selectAllBooks();

    List<BookStock> selectAllBookStocks();

}
