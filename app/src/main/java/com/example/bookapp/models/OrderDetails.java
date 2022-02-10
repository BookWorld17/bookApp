package com.example.bookapp.models;

import android.content.Context;

import java.io.Serializable;

public class OrderDetails implements Serializable {
    String id;
    String buyer_id;
    String book_copy_id;
    String order_date;
    String qty;
    String orderStatus;
    Book book;
    User seller;
    String orderDate;
    String deliveryDate;
    String deliveryOption;
    String deliveryAddress;

    public OrderDetails(String id,
                        String buyer_id,
                        String book_copy_id,
                        String order_date,
                        String qty,
                        String orderStatus,
                        String orderDate,
                        String deliveryDate,
                        String deliveryOption,
                        String deliveryAddress,
                        Book book,
                        User seller){
        this.id = id;
        this. buyer_id = buyer_id;
        this. book_copy_id=  book_copy_id;
        this. order_date = order_date;
        this. qty = qty;
        this.  orderStatus = orderStatus;
        this.  book = book;
        this.  orderDate = orderDate;
        this. deliveryDate = deliveryDate;
        this. deliveryOption = deliveryOption;
        this. deliveryAddress = deliveryAddress;
        this.seller = seller;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public void setBook_copy_id(String book_copy_id) {
        this.book_copy_id = book_copy_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setDeliveryOption(String deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public User getSeller() {
        return seller;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getOrder_date() {
        return order_date;
    }

    public Book getBook() {
        return book;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getBook_copy_id() {
        return book_copy_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public String getQty() {
        return qty;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }



    public String getStatusText(Context context) {
        String statusTxt = "";
        if(getOrderStatus().equals("0"))
        {
            statusTxt = "Order submitted";
        }else if(getOrderStatus().equals("1"))
        {
            statusTxt = "confirmed";
        }else if(getOrderStatus().equals("2"))
        {
            statusTxt = "out for delivery";
        }else if(getOrderStatus().equals("3"))
        {
            statusTxt = "delivered";
        }
        return statusTxt;
    }

}
