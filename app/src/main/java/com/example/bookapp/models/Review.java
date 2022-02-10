package com.example.bookapp.models;

public class Review {
    String id;
    String buyer_id;
    String book_id;
    String comment;
    String rate;
    String rev_date;

    public Review(String id, String buyer_id, String book_id, String comment, String rate, String rev_date){
        this.id = id;
        this.buyer_id = buyer_id;
        this.book_id = book_id;
        this.comment = comment;
        this.rate = rate;
        this.rev_date = rev_date;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public String getComment() {
        return comment;
    }

    public String getRate() {
        return rate;
    }


    public String getRev_date() {
        return rev_date;
    }


}
