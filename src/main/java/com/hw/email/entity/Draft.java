package com.hw.email.entity;

import java.util.ArrayList;
import java.util.List;

public class Draft {
    private int id;

    private String title;
    private List<String> recipientList = new ArrayList<>();
    private String sender;
    private String content;

    private long timeCreated;

    public Draft() {
    }

    public Draft(int id, String title, List<String> recipient, String sender, String content, long timeCreated) {
        this.id = id;
        this.title = title;
        this.recipientList = recipient;
        this.sender = sender;
        this.content = content;
        this.timeCreated = timeCreated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getRecipientList() {
        return recipientList;
    }

    public void setRecipientList(List<String> recipientList) {
        this.recipientList = recipientList;
    }

    public void addRecipient(String recipient) {
        if (!recipientList.contains(recipient)) {
            recipientList.add(recipient);
        }
    }

    public void removeRecipient(String recipient) {
        recipientList.remove(recipient);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }
}
