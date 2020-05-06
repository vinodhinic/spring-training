package com.kindle.domain;

public class Book {
    private String isbn;
    private String bookName;
    private Integer price;

    public Book() {
        super();
    }

    public Book(String isbn, String bookName, Integer price) {
        this.isbn = isbn;
        this.bookName = bookName;
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", bookName='" + bookName + '\'' +
                ", price=" + price +
                '}';
    }
}
