package com.hw.email.dao.impl;

import com.hw.email.dao.EmailRepository;
import com.hw.email.entity.Draft;
import com.hw.email.entity.Email;
import com.hw.email.entity.User;
import com.hw.email.provider.StorageProvider;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailRepositoryLocal implements EmailRepository {
    @Override
    public void addEmail(String userId, Email email) {
        StorageProvider.addEmail(userId, email);
    }

    @Override
    public void addDraft(String userId, Draft draft) {
        StorageProvider.addDraft(userId, draft);
    }

    @Override
    public Map<Integer, Email> getUserEmailMap(String userId) {
        return StorageProvider.getUserEmailMap(userId);
    }

    @Override
    public Email getEmail(String userId, int emailId) {
        return StorageProvider.getEmail(userId, emailId);
    }

    @Override
    public Draft getDraft(String userId, int draftId) {
        return StorageProvider.getDraft(userId, draftId);
    }

    @Override
    public int getNextDraftIdByUser(String userId) {
        return StorageProvider.getNextDraftIdByUser(userId);
    }

    @Override
    public User getUser(String userId) {
        return StorageProvider.getUser(userId);
    }
}
