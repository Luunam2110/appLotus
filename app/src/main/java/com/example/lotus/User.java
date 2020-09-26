package com.example.lotus;

public class User {
    public String picture,username,phonenumber,id,status;

    public User() {
    }

    public User(String picture, String username, String phonenumber, String id, String status) {
        this.picture = picture;
        this.username = username;
        this.phonenumber = phonenumber;
        this.id = id;
        this.status = status;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
