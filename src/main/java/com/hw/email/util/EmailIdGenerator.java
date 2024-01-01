package com.hw.email.util;

import java.util.concurrent.atomic.AtomicInteger;

public class EmailIdGenerator {
    private EmailIdGenerator() {}
    private static final AtomicInteger currentEmailId = new AtomicInteger(0);
    public static Integer getEmailId() {
        return currentEmailId.incrementAndGet();
    }
}
