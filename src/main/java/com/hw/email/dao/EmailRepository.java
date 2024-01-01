package com.hw.email.dao;

import com.hw.email.entity.Draft;
import com.hw.email.entity.Email;
import com.hw.email.entity.User;

import java.util.Map;

public interface EmailRepository {
    void addEmail(String userId, Email email);

    void addDraft(String userId, Draft draft);

    Map<Integer, Email> getUserEmailMap(String userId);

    Email getEmail(String userId, int emailId);

    Draft getDraft(String userId, int draftId);

    int getNextDraftIdByUser(String userId);

    User getUser(String userId);

}
