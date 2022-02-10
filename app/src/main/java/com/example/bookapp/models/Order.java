package com.example.bookapp.models;

import android.content.Context;

import java.io.Serializable;

public class Order implements Serializable {
    String id;
    String buyer_id;
    String order_date;
    String orderStatus;
    User seller;
    String orderDate;
    String deliveryDate;
    String deliveryOption;
    String deliveryAddress;
    String disputeOpend = "0";

    public Order(String id,
                 String buyer_id,
                 String order_date,
                 String orderStatus,
                 String orderDate,
                 String deliveryDate,
                 String deliveryOption,
                 String deliveryAddress,
                 User seller){
        this.id = id;
        this. buyer_id = buyer_id;
        this. order_date = order_date;
        this.  orderStatus = orderStatus;
        this.  orderDate = orderDate;
        this. deliveryDate = deliveryDate;
        this. deliveryOption = deliveryOption;
        this. deliveryAddress = deliveryAddress;
        this.seller = seller;
    }

    public String getDisputeOpend() {
        return disputeOpend;
    }

    public void setDisputeOpend(String disputeOpend) {
        this.disputeOpend = disputeOpend;
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


    public String getOrder_date() {
        return order_date;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getBuyer_id() {
        return buyer_id;
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
