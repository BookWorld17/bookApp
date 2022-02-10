package com.example.bookapp.models;


import java.io.Serializable;

public class BookCopy implements Serializable {
    public String ID;
    public String book_id;
    public String isbn;
    public String price;
    public String numberOfCopies;
    public String numberOfPages;
    public String addDate;

    public BookCopy() {
    }

    public BookCopy(String id
                , String book_id
                        , String isbn
                        , String price
                        , String numberOfCopies
                        , String numberOfPages
                        , String addDate ) {
        this.ID = id;
        this.book_id = book_id;
        this.isbn = isbn;
        this.price = price;
        this.numberOfCopies = numberOfCopies;
        this.numberOfPages = numberOfPages;
        this.addDate = addDate;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getID() {
        return ID;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setNumberOfCopies(String numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public void setNumberOfPages(String numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getPrice() {
        return price;
    }

    public String getBook_id() {
        return book_id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getNumberOfCopies() {
        return numberOfCopies;
    }

    public String getNumberOfPages() {
        return numberOfPages;
    }


    @Override
    public String toString() {
        return   isbn ;
    }
}
