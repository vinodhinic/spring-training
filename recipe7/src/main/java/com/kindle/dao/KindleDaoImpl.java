package com.kindle.dao;

import com.kindle.domain.AmazonPay;
import com.kindle.domain.Book;
import com.kindle.domain.BookStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class KindleDaoImpl extends NamedParameterJdbcDaoSupport implements KindleDao {

    private static final String SQL_INSERT_BOOK =
            "insert into book (isbn, book_name, price) values (:isbn, :bookName, :price)";
    private final String SQL_INSERT_BOOK_STOCK = "insert into book_stock (isbn, stock) values (:isbn, :stock)";
    private final String SQL_INSERT_AMAZON_PAY = "insert into amazon_pay (username, balance) values(:userName, :balance)";

    private final String SQL_SELECT_ALL_BOOKS = "select * from book";
    private final String SQL_SELECT_ALL_BOOK_STOCKS = "select * from book_stock";
    private final String SQL_SELECT_ALL_AMAZON_PAY = "select * from amazon_pay";

    private final String SQL_DELETE_ALL_BOOKS = "delete from book";
    private final String SQL_DELETE_ALL_BOOK_STOCKS = "delete from book_stock";
    private final String SQL_DELETE_ALL_AMAZON_PAY = "delete from amazon_pay";

    @Autowired
    public KindleDaoImpl(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public boolean insertBook(Book book) {
        SqlParameterSource parameterSource =
                new BeanPropertySqlParameterSource(book);
        return getNamedParameterJdbcTemplate().update(SQL_INSERT_BOOK, parameterSource) > 0;
    }

    @Override
    public boolean insertBookStock(BookStock bookStock) {
        SqlParameterSource parameterSource =
                new BeanPropertySqlParameterSource(bookStock);
        return getNamedParameterJdbcTemplate().update(SQL_INSERT_BOOK_STOCK, parameterSource) > 0;
    }

    @Override
    public boolean insertAccount(AmazonPay amazonPay) {
        SqlParameterSource parameterSource =
                new BeanPropertySqlParameterSource(amazonPay);
        return getNamedParameterJdbcTemplate().update(SQL_INSERT_AMAZON_PAY, parameterSource) > 0;
    }

    @Override
    public void deleteAllBookAndStock() {
        getJdbcTemplate().update(SQL_DELETE_ALL_BOOK_STOCKS);
        getJdbcTemplate().update(SQL_DELETE_ALL_BOOKS);
    }

    @Override
    public void deleteAllAccounts() {
        getJdbcTemplate().update(SQL_DELETE_ALL_AMAZON_PAY);
    }

    @Override
    public List<Book> selectAllBooks() {
        return getJdbcTemplate().query(SQL_SELECT_ALL_BOOKS, BeanPropertyRowMapper.newInstance(Book.class));
    }

    @Override
    public List<BookStock> selectAllBookStocks() {
        return getJdbcTemplate().query(SQL_SELECT_ALL_BOOK_STOCKS, BeanPropertyRowMapper.newInstance(BookStock.class));
    }

    @Override
    public List<AmazonPay> selectAllAmazonPay() {
        return getJdbcTemplate().query(SQL_SELECT_ALL_AMAZON_PAY, BeanPropertyRowMapper.newInstance(AmazonPay.class));
    }
}
