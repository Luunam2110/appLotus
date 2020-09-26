package com.example.lotus;

import com.google.firebase.database.DatabaseReference;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Chat implements Comparable<Chat> {
    String lastsender,lastmess,picture,name,chatid,time;

    public String getLastsender() {
        return lastsender;
    }

    public void setLastsender(String lastsender) {
        this.lastsender = lastsender;
    }

    public String getLastmess() {
        return lastmess;
    }

    public void setLastmess(String lastmess) {
        this.lastmess = lastmess;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Chat(String lastsender, String lastmess, String picture, String name, String chatid, String time) {
        this.lastsender = lastsender;
        this.lastmess = lastmess;
        this.picture = picture;
        this.name = name;
        this.time=time;
        this.chatid = chatid;
    }

    public Chat(){

    }

    @Override
    public int compareTo(Chat o) {
        SimpleDateFormat fomartdate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS");
        Date otime= new Date(),mytime= new Date();
        try {
            mytime = fomartdate.parse(this.time);
            otime = fomartdate.parse(o.time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mytime.compareTo(otime);
    }
}
