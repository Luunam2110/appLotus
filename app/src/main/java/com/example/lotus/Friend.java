package com.example.lotus;

public class Friend {
    String id,picture,chat,name;

    public Friend(String id, String picture, String chat, String name) {
        this.id = id;
        this.picture = picture;
        this.chat = chat;
        this.name = name;
    }
    public  Friend(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
