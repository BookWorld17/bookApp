package com.example.bookapp.models;

import android.content.Context;


import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {
    public String ID;
    public String bookName;
    public String authorName;
    public String publisherName;
    public String summary;
    public String seller_id;
    public String addDate;
    public String bookType;
    public String image;
    public String category_id;

    ArrayList<BookCopy> copyArrayList = new ArrayList<>();
    public Book() {
    }

    public Book(String id
                ,String bookName
                ,String authorName
                ,String publisherName
                ,String summary
                ,String seller_id
                ,String addDate
                ,String bookType
                ,String image
                ,String category_id)
    {
        this.ID = id;
        this.bookName = bookName;
        this.authorName = authorName;
        this.publisherName = publisherName;
        this.summary = summary;
        this.seller_id = seller_id;
        this.addDate = addDate;
        this.bookType = bookType;
        this.image = image;
        this.category_id = category_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getID() {
        return ID;
    }

    public String getAddDate() {
        return addDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookType() {
        return bookType;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getSummary() {
        return summary;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCopyArrayList(ArrayList<BookCopy> copyArrayList) {
        this.copyArrayList = copyArrayList;
    }

    public ArrayList<BookCopy> getCopyArrayList() {
        return copyArrayList;
    }
}
