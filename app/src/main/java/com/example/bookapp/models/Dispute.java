package com.example.bookapp.models;

import android.content.Context;

import com.example.bookapp.R;

import java.io.Serializable;

public class Dispute implements Serializable {
    String id;
    String buyer_id;
    String order_id;
    String dispute_date;
    String description;
    String img;
    String status;
    String admin_decision;
    String decision_date;

    public Dispute(String id,
                   String buyer_id,
                   String order_id,
                   String dispute_date,
                   String description,
                   String img,
                   String status,
                   String admin_decision,
                   String decision_date
                   ){
        this.id = id;
        this. buyer_id = buyer_id;
        this. order_id=  order_id;
        this. dispute_date = dispute_date;
        this. description = description;
        this.  img = img;
        this.  status = status;
        this.  admin_decision = admin_decision;
        this.  decision_date = decision_date;

    }

    public String getAdmin_decision() {
        return admin_decision;
    }

    public String getDecision_date() {
        return decision_date;
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

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    public String getDispute_date() {
        return dispute_date;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getStatusText(Context context) {
        String statusTxt = "";
        if(getStatus().equals("0"))
        {
            statusTxt = context.getString(R.string.opened);
        }else if(getStatus().equals("1"))
        {
            statusTxt = context.getString(R.string.closed);
        }
        return statusTxt;
    }

}
