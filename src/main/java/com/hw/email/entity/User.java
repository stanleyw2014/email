package com.hw.email.entity;

import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private String userId;
    private AtomicInteger currentDraftId = new AtomicInteger(0);

    private String password;

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getNextDraftId() {
        return currentDraftId.incrementAndGet();
    }
}
