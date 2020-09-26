package com.example.lotus;

public class Messenger {
    String id,messenger, sender,time,seen;
    public Messenger(){};

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public Messenger(String id, String Messenger, String sender, String time, String seen) {
        this.id = id;
        this.seen=seen;
        this.messenger = Messenger;
        this.sender = sender;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
