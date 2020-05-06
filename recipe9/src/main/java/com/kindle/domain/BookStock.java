package com.kindle.domain;

public class BookStock {
    private String isbn;
    private Integer stock;

    public BookStock() {
        super();
    }

    public BookStock(String isbn, Integer stock) {
        this.isbn = isbn;
        this.stock = stock;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "BookStock{" +
                "isbn='" + isbn + '\'' +
                ", stock=" + stock +
                '}';
    }
}
