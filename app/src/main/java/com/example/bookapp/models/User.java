package com.example.bookapp.models;

import java.io.Serializable;

public class User implements Serializable {
    String id;
    String name;
    String email;
    String address;

    public User(String id, String name, String email, String address){
        this.id = id;
        this.address = address;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
