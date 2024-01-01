package com.hw.email.entity;

import java.util.List;

public class Email extends Draft {

    private long timeSent;


    public Email(int id, String title, List<String> recipient, String sender, String content, long timeCreated, long timeSent) {
        super(id, title, recipient, sender, content, timeCreated);
        this.timeSent = timeSent;
    }

    public long getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(long timeSent) {
        this.timeSent = timeSent;
    }
}
