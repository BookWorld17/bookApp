package com.example.bookapp.models;

import java.io.Serializable;

public class Cart implements Serializable {
    String id;
    Book book;
    int qty;

    public Cart(String id, Book book, int qty){
        this.id = id;
        this.book = book;
        this.qty = qty;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public int getQty() {
        return qty;
    }

    public double getTotalPrice(){
        double res = qty * Double.parseDouble(book.getCopyArrayList().get(0).getPrice());
        return  res;
    }

}
